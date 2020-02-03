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
            FileSystemStore(libraryDir = File(config.getString("app.library-dir")))
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
                scanEnabled = config.getBoolean("app.scan-enabled")
            )
        }
        bean {
            BookIndex(IndexManager(config.getString("app.index-dir")))
        }
        bean {
            BookScanner(
                libraryDir = File(config.getString("app.library-dir")),
                workingDir = File(config.getString("app.working-dir"))
            )
        }
    }
}
