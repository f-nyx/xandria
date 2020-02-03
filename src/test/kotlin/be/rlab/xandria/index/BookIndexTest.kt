package be.rlab.xandria.index

import be.rlab.nlp.model.Language
import be.rlab.search.IndexManager
import org.junit.Before
import org.junit.Test
import java.io.File

class BookIndexTest {
    private val libraryDir: File = File("src/test/resources/library/")
    private val workingDir: File = File("./data")
    private val indexDir: File = File(workingDir, "test-index")
    private lateinit var indexManager: IndexManager

    @Before
    fun setUp() {
        indexDir.deleteRecursively()
        indexManager = IndexManager(indexDir.absolutePath)
    }

    @Test
    fun scanAndIndex() {
        val scanner = BookScanner(libraryDir, workingDir)
        val index = BookIndex(indexManager)
        index.index(scanner.scan { true }.toList().single().book)
        index.sync()

        val results = index.find(Language.SPANISH) {
            title("subsuelo")
        }.toList()
        assert(results.size == 1)
    }
}