package be.rlab.xandria

import be.rlab.tehanu.SpringApplication
import be.rlab.tehanu.config.TelegramBeans
import be.rlab.tehanu.support.persistence.DataSourceInitializer
import be.rlab.xandria.config.ApplicationBeans
import be.rlab.xandria.config.WebConfig
import be.rlab.xandria.domain.BookService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.getBean
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.netty.http.server.HttpServer

class Main(private val port: Int = 8945) : SpringApplication() {

    private val logger: Logger = LoggerFactory.getLogger(Main::class.java)

    override fun initialize() {
        applicationContext.apply {
            ApplicationBeans.beans(resolveConfig()).initialize(this)
            TelegramBeans.beans(resolveConfig()).initialize(this)
            register(WebConfig::class.java)
        }
    }

    override fun ready() {
        val bookService: BookService = applicationContext.getBean()
        val dataSourceInitializer: DataSourceInitializer = applicationContext.getBean()

        dataSourceInitializer.initAuto()
        bookService.scanAndIndex()

        val httpHandler: HttpHandler = WebHttpHandlerBuilder
            .applicationContext(applicationContext)
            .build()

        logger.info("Starting web server")
        HttpServer.create()
            .port(port)
            .handle(ReactorHttpHandlerAdapter(httpHandler))
            .bindNow()
        logger.info("Application started at http://localhost:$port")
    }
}

fun main() {
    Main().start()
}
