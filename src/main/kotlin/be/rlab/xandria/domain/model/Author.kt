package be.rlab.xandria.domain.model

import java.util.*

data class Author(
    val id: UUID,
    val name: String
) {
    companion object {
        fun new(name: String): Author = Author(
            id = UUID.randomUUID(),
            name = name
        )
    }
}
