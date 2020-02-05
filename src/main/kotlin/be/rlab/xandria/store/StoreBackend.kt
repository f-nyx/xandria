package be.rlab.xandria.store

import org.springframework.core.io.Resource
import java.io.Closeable

/** Must be implemented by classes to manage books libraries from different type
 * of locations.
 */
interface StoreBackend : Closeable {
    /** Reads a resource with the specified id.
     * @param id Resource identifier.
     * @return the required resource.
     */
    fun read(id: String): Resource

    /** Lists all resources in this store.
     * @return a sequence to traverse the resource ids.
     */
    fun list(): Sequence<String>
}
