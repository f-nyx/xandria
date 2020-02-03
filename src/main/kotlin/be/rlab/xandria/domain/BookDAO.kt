package be.rlab.xandria.domain

import be.rlab.tehanu.support.ObjectMapperFactory
import be.rlab.tehanu.support.persistence.TransactionSupport
import be.rlab.xandria.domain.model.Book
import be.rlab.xandria.support.BookId
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.*

/** DAO to save and retrieve books from the database.
 */
class BookDAO : TransactionSupport() {

    companion object {
        private const val PAGE_SIZE: Int = 1000
    }

    private val objectMapper: ObjectMapper = ObjectMapperFactory.snakeCaseMapper

    fun get(hash: Int): Book = transaction {
        val bookEntity = BooksEntity.find {
            Books.hash eq hash
        }.single()
        objectMapper.readValue(bookEntity.data)
    }

    fun list(): Sequence<Book> = transaction {
        val results = BooksEntity.all().iterator()

        generateSequence {
            if (results.hasNext()) {
                transaction {
                    val bookEntity = results.next()
                    objectMapper.readValue<Book>(bookEntity.data)
                }
            } else {
                null
            }
        }
    }

    fun save(book: Book): Book = transaction {
        val bookHash: Int = BookId.generate(book)
        val results = BooksEntity.find {
            Books.hash eq bookHash
        }

        if (results.empty()) {
            BooksEntity.new(book.id) {
                hash = bookHash
                data = objectMapper.writeValueAsString(book)
            }
        }

        book
    }

    fun deleteAll() = transaction {
        Books.deleteAll()
    }
}
