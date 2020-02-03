package be.rlab.xandria.store

import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.io.File

class FileSystemStore(
    private val libraryDir: File
): StoreBackend {
    init {
        require(libraryDir.exists()) { "the library directory must exist." }
    }

    override fun read(id: String): Resource {
        val file = File(libraryDir, id)
        require(file.exists())
        return FileSystemResource(file)
    }
}
