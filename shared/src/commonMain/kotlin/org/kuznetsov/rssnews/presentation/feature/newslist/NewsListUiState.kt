package org.kuznetsov.rssnews.presentation.feature.newslist

import org.kuznetsov.rssnews.presentation.model.ArticleUi

data class NewsListUiState(
    val isLoading: Boolean = false,
    val articles: List<ArticleUi> = emptyList(),
    val errorMessage: String? = null,
)
