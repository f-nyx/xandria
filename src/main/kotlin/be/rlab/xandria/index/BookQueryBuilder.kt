package be.rlab.xandria.index

import be.rlab.search.model.QueryBuilder
import be.rlab.xandria.index.BookIndex.Companion.FIELD_AUTHOR_NAME
import be.rlab.xandria.index.BookIndex.Companion.FIELD_CATEGORY
import be.rlab.xandria.index.BookIndex.Companion.FIELD_DESCRIPTION
import be.rlab.xandria.index.BookIndex.Companion.FIELD_TITLE
import org.apache.lucene.search.BooleanClause
import org.apache.lucene.search.Query

/** Builder to create search queries for books.
 */
class BookQueryBuilder(
    private val queryBuilder: QueryBuilder
) {
    companion object {
        /** Creates a query builder for the specified language.
         * @param parent Parent query builder to add this query to.
         */
        fun query(parent: QueryBuilder): BookQueryBuilder {
            return BookQueryBuilder(parent)
        }
    }

    /** Creates a default search query for the specified term.
     *
     * The default query search for books that contains the specified term in
     * the title or description fields.
     *
     * @param criteria Search criteria.
     */
    fun default(criteria: String): BookQueryBuilder {
        title(criteria, BooleanClause.Occur.SHOULD)
        description(criteria, BooleanClause.Occur.SHOULD)
        return this
    }

    /** Search books by title.
     *
     * @param title Book title.
     * @param occur Boolean operator. Default is AND.
     * @return this builder.
     */
    fun title(
        title: String,
        occur: BooleanClause.Occur = BooleanClause.Occur.MUST
    ): BookQueryBuilder {
        queryBuilder.term(FIELD_TITLE, title, occur = occur)
        return this
    }

    /** Search books by description.
     *
     * @param description Book description.
     * @param occur Boolean operator. Default is AND.
     * @return this builder.
     */
    fun description(
        description: String,
        occur: BooleanClause.Occur = BooleanClause.Occur.MUST
    ): BookQueryBuilder {
        queryBuilder.term(FIELD_DESCRIPTION, description, occur = occur)
        return this
    }

    /** Search books by categories.
     *
     * This criteria can be specified several times to search for several categories.
     * By default categories are added to the query with an OR operator.
     *
     * @param category Book category.
     * @param occur Boolean operator. Default is OR.
     * @return this builder.
     */
    fun category(
        category: String,
        occur: BooleanClause.Occur = BooleanClause.Occur.SHOULD
    ): BookQueryBuilder {
        queryBuilder.term(FIELD_CATEGORY, category, occur = occur)
        return this
    }

    /** Search books by author name.
     *
     * @param author Author name.
     * @param occur Boolean operator. Default is AND.
     * @return this builder.
     */
    fun author(
        author: String,
        occur: BooleanClause.Occur = BooleanClause.Occur.MUST
    ): BookQueryBuilder {
        queryBuilder.term(FIELD_AUTHOR_NAME, author, occur = occur)
        return this
    }

    fun parse(query: String): BookQueryBuilder {
        queryBuilder.parse(FIELD_TITLE, query)
        return this
    }

    /** Builds the Lucene query.
     * @return a valid Lucene query.
     */
    fun build(): Query {
        return queryBuilder.build()
    }
}
