package be.rlab.xandria.domain.model

import be.rlab.nlp.model.Language
import org.joda.time.DateTime
import java.util.*

/** Represents a book in the library.
 */
data class Book(
    /** Book unique id. */
    val id: UUID,
    /** Book language. */
    val language: Language,
    /** Book title. */
    val title: String,
    /** Book description. */
    val description: String,
    /** Book author. */
    val author: Author,
    /** Book first edition date. */
    val publicationDate: DateTime,
    /** Book categories. */
    val categories: List<String>,
    /** Relative location within the library. */
    val location: String,
    /** Indicates whether the book has cover or not. */
    val cover: Boolean
) {
    companion object {
        fun new(
            language: Language,
            title: String,
            description: String,
            author: Author,
            publicationDate: DateTime,
            categories: List<String>,
            location: String,
            cover: Boolean
        ): Book = Book(
            id = UUID.randomUUID(),
            language = language,
            title = title,
            description = description,
            author = author,
            publicationDate = publicationDate,
            categories = categories,
            location = location,
            cover = cover
        )
    }
}
