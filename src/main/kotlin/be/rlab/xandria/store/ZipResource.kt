package be.rlab.xandria.store

import org.springframework.core.io.InputStreamResource
import java.io.InputStream
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.Charset

class ZipResource(
    input: InputStream,
    private val path: String
) : InputStreamResource(input) {
    override fun getURI(): URI {
        return URI.create(URLEncoder.encode(path, Charset.defaultCharset()))
    }
}
