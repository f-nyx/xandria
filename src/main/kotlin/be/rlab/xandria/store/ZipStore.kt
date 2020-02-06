package be.rlab.xandria.store

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipFile
import org.springframework.core.io.Resource
import java.io.File
import java.net.URLDecoder

class ZipStore(
    private val zipFile: File
): StoreBackend {
    init {
        require(zipFile.exists()) { "the file must exist." }
    }

    private val archive: ZipFile by lazy {
        ZipFile(zipFile)
    }

    override fun list(): Sequence<String> {
        val entries = archive.entries.iterator()

        return generateSequence {
            if (entries.hasNext()) {
                var entry: ZipArchiveEntry = entries.next()

                while (entry.isDirectory && entries.hasNext()) {
                    entry = entries.next()
                }

                if (!entry.isDirectory) {
                    entry.name
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

    override fun read(id: String): Resource {
        val path = try {
            URLDecoder.decode(id, "utf-8")
        } catch (cause: Exception) {
            id
        }
        val entry = archive.getEntry(path)
            ?: throw RuntimeException("entry not found: $path")
        return ZipResource(archive.getInputStream(entry).readBytes(), path)
    }

    override fun close() {
        archive.close()
    }
}
