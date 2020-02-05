package be.rlab.xandria.index

import be.rlab.nlp.model.Language
import be.rlab.xandria.domain.model.Author
import be.rlab.xandria.domain.model.Book
import be.rlab.xandria.index.model.ScanResult
import be.rlab.xandria.store.StoreBackend
import org.joda.time.DateTime
import org.springframework.core.io.Resource
import org.w3c.dom.Document
import java.io.File
import java.net.URLDecoder
import java.nio.charset.Charset
import org.joox.JOOX.`$` as J

/** Scans a directory to search for books.
 *
 * It looks for _.opf_ metadata files to detect EPUB.format books.
 */
class BookScanner(
    /** Directory to search for books. */
    private val store: StoreBackend,
    /** Directory to save scanner data. */
    workingDir: File
) {
    companion object {
        private const val OPF_EXTENSION: String = ".opf"
        private const val CACHE_FILE: String = "file-list.txt"
    }

    init {
        require(workingDir.exists()) { "the working directory must exist " }
    }

    private val opfCache: File = File(workingDir, CACHE_FILE)
    private val authors: MutableMap<String, Author> = mutableMapOf()
    private val languages: Map<String, Language> = mapOf(
        "spa" to Language.SPANISH,
        "eng" to Language.ENGLISH,
        "fra" to Language.FRENCH,
        "por" to Language.PORTUGUESE,
        "pol" to Language.POLISH
    )

    /** Scans the library directory and produces a sequence of books.
     *
     * It creates a cache of existing metadata files before reading them. Next times it won't traverse
     * the library directory unless the [CACHE_FILE] is removed.
     *
     * @param accept Function to determine whether to read a resource or not.
     * @return a sequence of books in the library directory.
     */
    fun scan(accept: (String) -> Boolean): Sequence<ScanResult> {
        createOpfCacheIfRequired()

        val resources: Iterator<String> = opfCache.readLines().iterator()

        return generateSequence {
            if (resources.hasNext()) {
                var nextResource: String = resources.next()

                while (!accept(nextResource) && resources.hasNext()) {
                    nextResource = resources.next()
                }

                if (accept(nextResource)) {
                    val book = readMetadata(store.read(nextResource))

                    ScanResult.new(
                        resource = nextResource,
                        book = book,
                        error = false
                    )
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

    private fun readMetadata(opfResource: Resource): Book {
        val doc: Document = J(opfResource.inputStream).document()
        val authorName: String = J(doc).find("creator").text()
        val language: String = J(doc).find("language").text()

        if (!authors.containsKey(authorName)) {
            authors[authorName] = Author.new(authorName)
        }

        return Book.new(
            language = languages[language]
                ?: throw RuntimeException("language not supported: $language"),
            title = J(doc).find("title").text(),
            description = J(doc).find("description").text() ?: "",
            author = authors.getValue(authorName),
            publicationDate = DateTime.parse(J(doc).find("date").text()),
            categories = J(doc).find("subject").map { categoryEl ->
                J(categoryEl).text()
            },
            location = opfResource.uri.path.substringBeforeLast("/"),
            cover = J(doc).find("reference")?.attr("type") == "cover"
        )
    }

    private fun createOpfCacheIfRequired() {
        if (!opfCache.exists()) {
            val filesCache: String = store.list().filter { resource ->
                resource.endsWith(OPF_EXTENSION)
            }.joinToString("\n")

            opfCache.writeText(filesCache)
        }
    }
}
