package org.kuznetsov.rssnews.presentation.feature

import org.kuznetsov.rssnews.presentation.model.ArticleUi

/** Matches the query against a story's headline, category and byline (author). */
internal fun List<ArticleUi>.filterByQuery(query: String): List<ArticleUi> {
    if (query.isBlank()) return this
    return filter { article ->
        article.headline.contains(query, ignoreCase = true) ||
            article.category.contains(query, ignoreCase = true) ||
            article.byline.contains(query, ignoreCase = true)
    }
}
