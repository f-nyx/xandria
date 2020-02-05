package be.rlab.xandria.store

import org.springframework.core.io.ByteArrayResource
import java.io.InputStream
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.Charset

class ZipResource(
    input: InputStream,
    private val path: String
) : ByteArrayResource(input.readAllBytes()) {
    override fun getURI(): URI {
        return URI.create(URLEncoder.encode(path, Charset.defaultCharset()))
    }
}
