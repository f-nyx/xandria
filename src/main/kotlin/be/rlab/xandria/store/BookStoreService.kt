package be.rlab.xandria.store

import be.rlab.xandria.domain.model.Book
import be.rlab.xandria.support.BookId
import org.springframework.core.io.Resource
import java.net.URI
import java.net.URL

class BookStoreService(
    private val store: StoreBackend,
    private val serviceUrl: String
) {
    fun getCover(book: Book): Resource {
        return store.read("${book.location}/cover.jpg")
    }

    fun getCoverUrl(book: Book): URL {
        val baseUrl: String = serviceUrl.removeSuffix("/")
        val bookId: Int = BookId.generate(book)
        return URI.create("$baseUrl/books/$bookId/cover").toURL()
    }
}
