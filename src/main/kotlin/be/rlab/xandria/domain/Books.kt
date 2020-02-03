package be.rlab.xandria.domain

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import java.util.*

object Books : UUIDTable(name = "books") {
    val hash = integer("hash").index()
    val data = text("data")
}

class BooksEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BooksEntity>(Books)
    var hash: Int by Books.hash
    var data: String by Books.data
}
