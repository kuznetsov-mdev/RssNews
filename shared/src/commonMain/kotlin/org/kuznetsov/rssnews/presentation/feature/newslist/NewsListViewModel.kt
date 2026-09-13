package org.kuznetsov.rssnews.presentation.feature.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsState
import org.kuznetsov.rssnews.presentation.model.ArticleUi
import org.kuznetsov.rssnews.presentation.model.toArticleUi
import rssnews.shared.generated.resources.Res
import rssnews.shared.generated.resources.error_failed_to_load_news

class NewsListViewModel(
    private val repositoryApi: NewsRepositoryApi
) : ViewModel() {

    private val _state = MutableStateFlow(NewsListUiState(isLoading = true))
    val state: StateFlow<NewsListUiState> = _state

    private var newsById: Map<String, NewsState> = emptyMap()

    init {
        loadNews()
    }

    fun onToggleFavourite(article: ArticleUi) {
        val news = newsById[article.id] ?: return
        viewModelScope.launch {
            if (article.isFavorite) {
                repositoryApi.removeFromFavourite(news.id)
            } else {
                repositoryApi.addToFavourite(news)
            }
        }
    }

    private fun loadNews() {
        repositoryApi.findAll()
            .onEach { page ->
                newsById = page.items.associateBy { it.id.id }
                _state.value = NewsListUiState(
                    isLoading = false,
                    articles = page.items.map { it.toArticleUi() },
                )
            }
            .catch {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = getString(Res.string.error_failed_to_load_news),
                )
            }
            .launchIn(viewModelScope)
    }
}
