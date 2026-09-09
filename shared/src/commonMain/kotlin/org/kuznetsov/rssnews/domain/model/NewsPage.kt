package org.kuznetsov.rssnews.domain.model

data class NewsPage(
    val items: List<NewsState>,
    val nextPage: String? = null
)
