package be.rlab.xandria.command

import be.rlab.nlp.model.Language
import be.rlab.tehanu.annotations.InlineQueryHandler
import be.rlab.tehanu.annotations.MessageListener
import be.rlab.tehanu.messages.model.InlineQuery
import be.rlab.tehanu.messages.model.InlineQueryResponse
import be.rlab.xandria.domain.BookService
import be.rlab.xandria.store.BookStoreService
import me.ivmg.telegram.entities.ParseMode
import me.ivmg.telegram.entities.inlinequeryresults.InlineQueryResult
import me.ivmg.telegram.entities.inlinequeryresults.InputMessageContent

@MessageListener("book_search")
class BookSearch(
    private val bookService: BookService,
    private val bookStore: BookStoreService
) {

    companion object {
        private const val BOOKS_LIMIT = 10
    }

    @InlineQueryHandler
    fun search(inlineQuery: InlineQuery): InlineQueryResponse {
        val booksResult = bookService.search(Language.SPANISH, inlineQuery.offset, BOOKS_LIMIT) {
            title(inlineQuery.query)
        }
        val results = booksResult.books.toList().map { book ->
            val thumbUrl: String? = if (book.cover) {
                bookStore.getCoverUrl(book).toString()
            } else {
                null
            }

            InlineQueryResult.Article(
                id = book.id.toString(),
                title = book.title,
                description = book.description,
                inputMessageContent = InputMessageContent.Text(
                    "*${book.title}*\n_${book.author.name}_\n\n${book.description}",
                    parseMode = ParseMode.MARKDOWN
                ),
                thumbUrl = thumbUrl
            )
        }

        return InlineQueryResponse(
            nextOffset = booksResult.nextOffset,
            results = results,
            personal = false
        )
    }
}
