package be.rlab.xandria.controller

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class DownloadBookController {

    @GetMapping("/")
    fun home(model: Model): String {
        return "hello"
    }
}
