package be.rlab.xandria.domain

import be.rlab.nlp.model.Language
import be.rlab.search.model.Cursor
import be.rlab.search.model.SearchResult
import be.rlab.xandria.domain.model.Book
import be.rlab.xandria.domain.model.BookSearchResult
import be.rlab.xandria.index.BookIndex
import be.rlab.xandria.index.BookQueryBuilder
import be.rlab.xandria.index.BookScanner
import be.rlab.xandria.index.ScanResultDAO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BookService(
    private val bookIndex: BookIndex,
    private val bookDAO: BookDAO,
    private val scanResultDAO: ScanResultDAO,
    private val scanner: BookScanner,
    private val scanEnabled: Boolean
) {

    private val logger: Logger = LoggerFactory.getLogger(BookService::class.java)

    fun search(
        language: Language,
        offset: String,
        limit: Int,
        callback: BookQueryBuilder.() -> Unit
    ): BookSearchResult {
        val cursor: Cursor = deserialize(offset)
        val results: SearchResult = bookIndex.search(language, cursor, limit, callback)
        val books = results.docs.map { bookDoc ->
            bookDAO.get(bookDoc[BookIndex.FIELD_HASH]!!)
        }

        return BookSearchResult(
            books = books,
            nextOffset = serialize(results.next, offset)
        )
    }

    /** Retrieves a book by its hash.
     * @param bookHash Required book hash.
     * @return the required book.
     */
    fun getByHash(bookHash: Int): Book {
        return bookDAO.get(bookHash)
    }

    /** Scans a book library and adds books to the index.
     *
     * It will not add books that already exist in the index.
     */
    fun scanAndIndex() = GlobalScope.launch {
        if (scanEnabled) {
            scanner.scan { resource ->
                !scanResultDAO.exists(resource)
            }.forEach { result ->
                val book: Book = result.book

                logger.info("saving book: ${book.author.name} - ${book.title}")

                if (bookDAO.save(book)) {
                    // First-pass indexing. If the book is created, it adds the book to
                    // the index to make it available in real time.
                    bookIndex.index(book)
                }

                scanResultDAO.save(result)
            }
        }

        logger.info("indexing")
        bookDAO.list().forEach { book ->
            bookIndex.index(book)
        }
        bookIndex.sync()
        logger.info("scan finished")
    }

    private fun serialize(
        cursor: Cursor?,
        defaultOffset: String
    ): String {
        return cursor?.let {
            "${cursor.docId}|${cursor.score}|${cursor.shardIndex}"
        } ?: defaultOffset
    }

    private fun deserialize(offset: String): Cursor {
        val values: List<String> = offset.split("|")

        return if (values.size < 3) {
            Cursor.first()
        } else {
            Cursor(
                docId = values[0].toInt(),
                score = values[1].toFloat(),
                shardIndex = values[2].toInt()
            )
        }
    }
}
