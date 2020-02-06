package be.rlab.xandria.config

import be.rlab.search.IndexManager
import be.rlab.xandria.command.BookSearch
import be.rlab.xandria.domain.BookService
import be.rlab.xandria.domain.BookDAO
import be.rlab.xandria.domain.Books
import be.rlab.xandria.index.BookIndex
import be.rlab.xandria.index.BookScanner
import be.rlab.xandria.index.ScanResultDAO
import be.rlab.xandria.index.ScanResults
import be.rlab.xandria.store.BookStoreService
import be.rlab.xandria.store.ZipStore
import be.rlab.xandria.store.FileSystemStore
import com.typesafe.config.Config
import org.springframework.context.support.beans
import java.io.File

object ApplicationBeans {

    fun beans(config: Config) = beans {
        // Listeners
        bean<BookSearch>()

        // Data access
        bean { Books }
        bean { ScanResults }
        bean<BookDAO>()
        bean<ScanResultDAO>()

        // Store
        bean {
            val location: String = config.getString("app.library-location")
            val protocol: String = location.substringBefore("://")
            val path: String = location.substringAfter("://")

            when (protocol) {
                "file" ->
                    FileSystemStore(libraryDir = File(path))
                "zip" ->
                    ZipStore(zipFile = File(path))
                else ->
                    throw RuntimeException("protocol not supported: $protocol")
            }
        }
        bean {
            BookStoreService(
                store = ref(),
                serviceUrl = config.getString("app.service-url")
            )
        }

        // Domain services
        bean {
            BookService(
                bookIndex = ref(),
                bookDAO = ref(),
                scanResultDAO = ref(),
                scanner = ref(),
                scanEnabled = config.getBoolean("app.scan-enabled"),
                indexingEnabled = config.getBoolean("app.indexing-enabled")
            )
        }
        bean {
            BookIndex(IndexManager(config.getString("app.index-dir")))
        }
        bean {
            BookScanner(
                workingDir = File(config.getString("app.working-dir")),
                store = ref()
            )
        }
    }
}
