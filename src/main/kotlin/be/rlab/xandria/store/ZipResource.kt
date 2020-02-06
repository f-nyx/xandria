package be.rlab.xandria.store

import org.springframework.core.io.ByteArrayResource
import java.net.URI
import java.net.URLEncoder

class ZipResource(
    input: ByteArray,
    private val path: String
) : ByteArrayResource(input) {
    override fun isOpen(): Boolean {
        return true
    }

    override fun isReadable(): Boolean {
        return true
    }

    override fun getURI(): URI {
        return URI.create(URLEncoder.encode(path, "utf-8"))
    }
}
