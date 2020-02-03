package be.rlab.xandria.index.model

import be.rlab.xandria.domain.model.Book
import java.util.*

data class ScanResult(
    val id: UUID,
    val resource: String,
    val book: Book,
    val error: Boolean
) {
    companion object {
        fun new(
            resource: String,
            book: Book,
            error: Boolean
        ): ScanResult = ScanResult(
            id = UUID.randomUUID(),
            resource = resource,
            book = book,
            error = error
        )
    }
}
