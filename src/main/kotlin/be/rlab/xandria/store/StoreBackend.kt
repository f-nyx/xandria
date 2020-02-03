package be.rlab.xandria.store

import org.springframework.core.io.Resource

interface StoreBackend {
    fun read(id: String): Resource
}
