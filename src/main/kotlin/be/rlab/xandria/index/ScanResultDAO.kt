package be.rlab.xandria.index

import be.rlab.tehanu.support.persistence.TransactionSupport
import be.rlab.xandria.index.model.ScanResult

/** DAO to save and retrieve scan results.
 */
class ScanResultDAO : TransactionSupport() {

    fun exists(resource: String): Boolean = transaction {
        !ScanResultEntity.find {
            ScanResults.resource eq resource
        }.empty()
    }

    fun save(scanResult: ScanResult): ScanResult = transaction {
        val results = ScanResultEntity.find {
            ScanResults.resource eq scanResult.resource
        }

        if (results.empty()) {
            ScanResultEntity.new(scanResult.id) {
                resource = scanResult.resource
                error = scanResult.error
            }
        }

        scanResult
    }
}
