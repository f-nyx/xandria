package be.rlab.xandria.support

import be.rlab.xandria.domain.model.Book
import org.apache.commons.codec.digest.MurmurHash3

object BookId {
    fun generate(book: Book): Int {
        return MurmurHash3.hash32("${book.author.name}-${book.title}")
    }
}
