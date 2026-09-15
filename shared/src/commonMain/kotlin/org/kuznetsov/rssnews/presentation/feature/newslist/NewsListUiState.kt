package org.kuznetsov.rssnews.presentation.feature.newslist

import org.kuznetsov.rssnews.presentation.model.ArticleUi

data class NewsListUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val query: String = "",
    val articles: List<ArticleUi> = emptyList(),
    val nextPage: String? = null,
    val errorMessage: String? = null,
)
