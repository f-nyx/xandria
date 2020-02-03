package be.rlab.xandria.domain.model

data class BookSearchResult(
    val books: List<Book>,
    val nextOffset: String
)
