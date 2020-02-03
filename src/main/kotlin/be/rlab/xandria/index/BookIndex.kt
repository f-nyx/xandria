package be.rlab.xandria.index

import be.rlab.nlp.model.Language
import be.rlab.search.IndexManager
import be.rlab.search.IndexManager.Companion.DEFAULT_LIMIT
import be.rlab.search.model.Cursor
import be.rlab.search.model.Document
import be.rlab.search.model.SearchResult
import be.rlab.xandria.domain.model.Book
import be.rlab.xandria.support.BookId

/** Manages the index to search for books.
 */
class BookIndex(
    private val indexManager: IndexManager
) {
    companion object {
        const val BOOKS_NAMESPACE: String = "Books"
        const val FIELD_ID: String = "uuid"
        const val FIELD_HASH: String = "hash"
        const val FIELD_TITLE: String = "title"
        const val FIELD_DESCRIPTION: String = "description"
        const val FIELD_CATEGORY: String = "category"
        const val FIELD_AUTHOR_NAME: String = "author_name"
        const val FIELD_AUTHOR_ID: String = "author_id"
    }

    /** Index a book.
     *
     * If the book is already indexed, it will not be indexed again.
     * The book data is not stored.
     *
     * @param book Book to index.
     * @return the book.
     */
    fun index(book: Book): Book {
        val bookId: Int = BookId.generate(book)
        val exists: Boolean = indexManager.count(BOOKS_NAMESPACE, book.language) {
            term(FIELD_HASH, bookId)
        } > 0

        if (!exists) {
            indexManager.index(BOOKS_NAMESPACE, book.language) {
                string(FIELD_ID, book.id.toString())
                int(FIELD_HASH, bookId) {
                    store(true)
                }
                text(FIELD_TITLE, book.title) {
                    store(false)
                }
                text(FIELD_DESCRIPTION, book.description) {
                    store(false)
                }
                text(FIELD_AUTHOR_NAME, book.author.name) {
                    store(false)
                }
                string(FIELD_AUTHOR_ID, book.author.id.toString()) {
                    store(false)
                }
                book.categories.forEach { category ->
                    text(FIELD_CATEGORY, category) {
                        store(false)
                    }
                }
            }
        }

        return book
    }

    /** Search for books.
     *
     * @param language Books language.
     * @param callback Callback to build the search query.
     * @return the paginated search results.
     */
    fun search(
        language: Language,
        cursor: Cursor = Cursor.first(),
        limit: Int = DEFAULT_LIMIT,
        callback: BookQueryBuilder.() -> Unit
    ): SearchResult {
        return indexManager.search(BOOKS_NAMESPACE, language, cursor, limit) {
            callback(BookQueryBuilder.query(this))
        }
    }

    /** Search for books.
     *
     * @param language Books language.
     * @param callback Callback to build the search query.
     * @return the paginated search results.
     */
    fun find(
        language: Language,
        callback: BookQueryBuilder.() -> Unit
    ): Sequence<Document> {
        return indexManager.find(BOOKS_NAMESPACE, language) {
            callback(BookQueryBuilder.query(this))
        }
    }

    /** Synchronizes the index saving all pending changes to disk.
     */
    fun sync() {
        indexManager.sync()
    }
}