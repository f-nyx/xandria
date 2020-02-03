package be.rlab.xandria.index

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import java.util.*

object ScanResults : UUIDTable(name = "scan_results") {
    val resource = varchar("resource", length = 255).index()
    val error = bool("is_error")
}

class ScanResultEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ScanResultEntity>(ScanResults)
    var resource: String by ScanResults.resource
    var error: Boolean by ScanResults.error
}
