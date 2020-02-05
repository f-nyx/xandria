package be.rlab.xandria.store

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.io.File
import java.net.URLDecoder
import java.nio.charset.Charset

class FileSystemStore(
    private val libraryDir: File
): StoreBackend {

    private val logger: Logger = LoggerFactory.getLogger(FileSystemStore::class.java)

    init {
        require(libraryDir.exists()) { "the library directory must exist." }
    }

    override fun list(): Sequence<String> {
        logger.info("scanning store")

        val filesIterator = libraryDir.walkTopDown().iterator()

        return generateSequence {
            if (filesIterator.hasNext()) {
                var file = filesIterator.next()

                while (!file.isFile && filesIterator.hasNext()) {
                    file = filesIterator.next()
                }

                if (file.isFile) {
                    file.absolutePath
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

    override fun read(id: String): Resource {
        logger.info("reading resouce: $id")

        val path = try {
            URLDecoder.decode(id, Charset.defaultCharset())
        } catch (cause: Exception) {
            id
        }
        val file = File(libraryDir, path
            .substringAfter("file://")
            .substringAfter(libraryDir.absolutePath)
        )
        require(file.exists())
        return FileSystemResource(file)
    }

    override fun close() {
        logger.info("closing store")
    }
}
