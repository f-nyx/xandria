package be.rlab.xandria.index.model

import be.rlab.nlp.model.Language
import be.rlab.xandria.index.BookIndex.Companion.FIELD_AUTHOR_NAME
import be.rlab.xandria.index.BookIndex.Companion.FIELD_CATEGORY
import be.rlab.xandria.index.BookIndex.Companion.FIELD_DESCRIPTION
import be.rlab.xandria.index.BookIndex.Companion.FIELD_TITLE

/** Index fields translations to support full-text search using fields in
 * different languages.
 */
object Fields {
    val translations: Map<Language, Map<String, String>> = mapOf(
        Language.ENGLISH to mapOf(
            FIELD_TITLE to "title",
            FIELD_DESCRIPTION to "description",
            FIELD_CATEGORY to "category",
            FIELD_AUTHOR_NAME to "author"
        ),
        Language.SPANISH to mapOf(
            FIELD_TITLE to "titulo",
            FIELD_DESCRIPTION to "description",
            FIELD_CATEGORY to "categoria",
            FIELD_AUTHOR_NAME to "autor"
        )
    )
}
