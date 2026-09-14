package org.kuznetsov.rssnews.presentation.feature.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsState
import org.kuznetsov.rssnews.presentation.model.ArticleUi
import org.kuznetsov.rssnews.presentation.model.toArticleUi
import rssnews.shared.generated.resources.Res
import rssnews.shared.generated.resources.error_failed_to_load_news

private const val SEARCH_DEBOUNCE_MS = 400L

class NewsListViewModel(
    private val repositoryApi: NewsRepositoryApi
) : ViewModel() {

    private val _state = MutableStateFlow(NewsListUiState(isLoading = true))
    val state: StateFlow<NewsListUiState> = _state

    // Sourced straight from local storage (NewsDao), independent of the remote feed's
    // load state — favourites must stay visible even when findAll()/findByQuery() fails.
    val favourites: StateFlow<List<ArticleUi>> = repositoryApi.getFavourites()
        .map { list -> list.map { it.toArticleUi() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val query = MutableStateFlow("")
    private var newsById: Map<String, NewsState> = emptyMap()

    init {
        observeNews()
    }

    fun onQueryChange(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
        query.value = newQuery
    }

    fun onToggleFavourite(article: ArticleUi) {
        viewModelScope.launch {
            if (article.isFavorite) {
                // Removing only needs the id, so this works even for articles that came
                // from the local favourites list rather than the currently-fetched feed.
                repositoryApi.removeFromFavourite(NewsId(article.id))
            } else {
                val news = newsById[article.id] ?: return@launch
                repositoryApi.addToFavourite(news)
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeNews() {
        query
            .debounce(SEARCH_DEBOUNCE_MS)
            .flatMapLatest { currentQuery ->
                if (currentQuery.isBlank()) repositoryApi.findAll() else repositoryApi.findByQuery(currentQuery)
            }
            .onEach { page ->
                newsById = page.items.associateBy { it.id.id }
                _state.update {
                    it.copy(
                        isLoading = false,
                        articles = page.items.map { news -> news.toArticleUi() },
                        errorMessage = null,
                    )
                }
            }
            .catch {
                _state.update {
                    it.copy(isLoading = false, errorMessage = getString(Res.string.error_failed_to_load_news))
                }
            }
            .launchIn(viewModelScope)
    }
}
