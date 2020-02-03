package be.rlab.xandria.controller

import be.rlab.xandria.domain.BookService
import be.rlab.xandria.domain.model.Book
import be.rlab.xandria.store.BookStoreService
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class CoverController(
    private val bookService: BookService,
    private val storeService: BookStoreService
) {
    @GetMapping("/books/{bookId}/cover", produces = ["image/jpeg"])
    fun retrieveCover(
        @PathVariable bookId: Int
    ): Resource {
        val book: Book = bookService.getByHash(bookId)

        return if (book.cover) {
            storeService.getCover(book)
        } else {
            throw RuntimeException("not found")
        }
    }
}
