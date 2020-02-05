package be.rlab.xandria.index

import be.rlab.nlp.model.Language
import be.rlab.xandria.store.FileSystemStore
import org.joda.time.DateTime
import org.junit.Test
import java.io.File

class BookScannerTest {
    private val libraryDir: File = File("src/test/resources/library/")
    private val workingDir: File = File("./data")

    @Test
    fun scan() {
        val reader = BookScanner(FileSystemStore(libraryDir), workingDir)
        reader.scan { true }.forEach { result ->
            val book = result.book

            assert(book.title == "Memorias del subsuelo")
            assert(book.description.contains("antihéroes de su ingente producción novelística"))
            assert(book.publicationDate == DateTime.parse("1864-01-01T00:00:00.000Z"))
            assert(book.categories.containsAll(listOf("Drama", "Filosófico", "Psicológico")))
            assert(book.author.name == "Fiódor Dostoyevski")
            assert(book.cover)
            assert(book.language == Language.SPANISH)

            println(book)
        }
    }
}
