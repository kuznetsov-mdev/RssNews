package org.kuznetsov.rssnews.presentation.feature.newslist

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.runner.RunWith
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsAuthor
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsPage
import org.kuznetsov.rssnews.domain.model.NewsState
import org.kuznetsov.rssnews.domain.model.NewsTitle
import org.kuznetsov.rssnews.domain.model.NewsTopic
import org.kuznetsov.rssnews.presentation.model.ArticleUi
import org.robolectric.RobolectricTestRunner
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

private fun newsState(id: String) = NewsState(
    id = NewsId(id),
    title = NewsTitle("Title $id"),
    topic = NewsTopic("Topic"),
    author = NewsAuthor("Author"),
    previewUrl = null,
)

private fun newsPage(ids: List<String>, nextPage: String? = null) =
    NewsPage(items = ids.map { newsState(it) }, nextPage = nextPage)

private fun article(id: String, isFavorite: Boolean = false) =
    ArticleUi(id = id, category = "Topic", headline = "Title $id", byline = "Author", isFavorite = isFavorite)

private class FakeNewsRepositoryApi(
    var findAllProvider: (String?) -> Flow<NewsPage> = { flowOf(NewsPage(emptyList())) },
    var findByQueryProvider: (String, String?) -> Flow<NewsPage> = { _, _ -> flowOf(NewsPage(emptyList())) },
) : NewsRepositoryApi {

    val favouritesFlow = MutableStateFlow<List<NewsState>>(emptyList())
    val addedToFavourite = mutableListOf<NewsState>()
    val removedFromFavourite = mutableListOf<NewsId>()
    var findAllCallCount = 0
        private set
    var findByQueryCallCount = 0
        private set
    var lastFindByQuery: String? = null
        private set

    override fun findAll(page: String?): Flow<NewsPage> {
        findAllCallCount++
        return findAllProvider(page)
    }

    override fun findByQuery(query: String, page: String?): Flow<NewsPage> {
        findByQueryCallCount++
        lastFindByQuery = query
        return findByQueryProvider(query, page)
    }

    override fun getFavourites(): Flow<List<NewsState>> = favouritesFlow

    override suspend fun addToFavourite(news: NewsState) {
        addedToFavourite += news
    }

    override suspend fun removeFromFavourite(newsId: NewsId) {
        removedFromFavourite += newsId
    }

    override suspend fun shareNews(news: NewsState) = Unit
}

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class NewsListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialStateIsLoadingWithNoArticles() = runTest(dispatcher) {
        val viewModel = NewsListViewModel(FakeNewsRepositoryApi())

        assertTrue(viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.articles.isEmpty())
    }

    @Test
    fun initialLoadPopulatesArticlesAndClearsLoading() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { flowOf(newsPage(listOf("all-1", "all-2"), nextPage = "cursor-1")) },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(listOf("all-1", "all-2"), state.articles.map { it.id })
        assertEquals("cursor-1", state.nextPage)
        assertEquals(1, repository.findAllCallCount)
    }

    @Test
    fun blankQueryRoutesToFindAll() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { flowOf(newsPage(listOf("all-1"))) },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }

        viewModel.onQueryChange("")
        advanceUntilIdle()

        assertEquals(0, repository.findByQueryCallCount)
        assertTrue(repository.findAllCallCount >= 1)
    }

    @Test
    fun nonBlankQueryRoutesToFindByQueryAfterDebounce() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { flowOf(newsPage(listOf("all-1"))) },
            findByQueryProvider = { _, _ -> flowOf(newsPage(listOf("query-1"))) },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onQueryChange("spacex")
        advanceTimeBy(200)
        assertEquals(listOf("all-1"), viewModel.state.value.articles.map { it.id })

        advanceUntilIdle()
        assertEquals(listOf("query-1"), viewModel.state.value.articles.map { it.id })
        assertEquals("spacex", repository.lastFindByQuery)
    }

    @Test
    fun rapidQueryChangesOnlyTriggerLastQueryAfterDebounce() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findByQueryProvider = { _, _ -> flowOf(newsPage(listOf("query-1"))) },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onQueryChange("s")
        advanceTimeBy(100)
        viewModel.onQueryChange("sp")
        advanceTimeBy(100)
        viewModel.onQueryChange("spa")
        advanceUntilIdle()

        assertEquals(1, repository.findByQueryCallCount)
        assertEquals("spa", repository.lastFindByQuery)
    }

    @Test
    fun repositoryErrorSetsErrorMessageAndClearsLoading() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { flow { throw RuntimeException("boom") } },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun favouritesOverlayUpdatesIsFavoriteWithoutResettingArticles() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { flowOf(newsPage(listOf("all-1", "all-2"), nextPage = "cursor-1")) },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        backgroundScope.launch { viewModel.favourites.collect() }
        advanceUntilIdle()

        repository.favouritesFlow.value = listOf(newsState("all-1"))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(listOf("all-1", "all-2"), state.articles.map { it.id })
        assertTrue(state.articles.single { it.id == "all-1" }.isFavorite)
        assertFalse(state.articles.single { it.id == "all-2" }.isFavorite)
        assertEquals("cursor-1", state.nextPage)
    }

    @Test
    fun loadMoreIsNoOpWhenNoNextPage() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { flowOf(newsPage(listOf("all-1"), nextPage = null)) },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.loadMore()
        advanceUntilIdle()

        assertEquals(1, repository.findAllCallCount)
        assertFalse(viewModel.state.value.isLoadingMore)
    }

    @Test
    fun loadMoreIsNoOpWhileAlreadyLoadingMore() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        var loadMoreCalls = 0
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { page ->
                if (page == "cursor-1") {
                    loadMoreCalls++
                    flow {
                        gate.await()
                        emit(newsPage(listOf("all-2"), nextPage = null))
                    }
                } else {
                    flowOf(newsPage(listOf("all-1"), nextPage = "cursor-1"))
                }
            },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.loadMore()
        runCurrent()
        assertTrue(viewModel.state.value.isLoadingMore)
        assertEquals(1, loadMoreCalls)

        viewModel.loadMore()
        runCurrent()
        assertEquals(1, loadMoreCalls)

        gate.complete(Unit)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoadingMore)
        assertEquals(listOf("all-1", "all-2"), viewModel.state.value.articles.map { it.id })
        assertEquals(1, loadMoreCalls)
    }

    @Test
    fun loadMoreSuccessAppendsItemsAndUpdatesNewsByIdCache() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { page ->
                if (page == "cursor-1") {
                    flowOf(newsPage(listOf("all-2"), nextPage = null))
                } else {
                    flowOf(newsPage(listOf("all-1"), nextPage = "cursor-1"))
                }
            },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.loadMore()
        advanceUntilIdle()

        assertEquals(listOf("all-1", "all-2"), viewModel.state.value.articles.map { it.id })
        assertNull(viewModel.state.value.nextPage)

        viewModel.onToggleFavourite(article("all-2", isFavorite = false))
        advanceUntilIdle()

        assertEquals(listOf(newsState("all-2")), repository.addedToFavourite)
    }

    @Test
    fun loadMoreFailureClearsLoadingMoreWithoutSettingErrorMessage() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { page ->
                if (page == "cursor-1") {
                    flow { throw RuntimeException("boom") }
                } else {
                    flowOf(newsPage(listOf("all-1"), nextPage = "cursor-1"))
                }
            },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.loadMore()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoadingMore)
        assertNull(state.errorMessage)
        assertEquals(listOf("all-1"), state.articles.map { it.id })
    }

    @Test
    fun toggleFavouriteOnFavoriteArticleRemovesFromFavourites() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { flowOf(newsPage(listOf("all-1"))) },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onToggleFavourite(article("all-1", isFavorite = true))
        advanceUntilIdle()

        assertEquals(listOf(NewsId("all-1")), repository.removedFromFavourite)
        assertTrue(repository.addedToFavourite.isEmpty())
    }

    @Test
    fun toggleFavouriteOnKnownNonFavoriteArticleAddsToFavourites() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi(
            findAllProvider = { flowOf(newsPage(listOf("all-1"))) },
        )
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onToggleFavourite(article("all-1", isFavorite = false))
        advanceUntilIdle()

        assertEquals(listOf(newsState("all-1")), repository.addedToFavourite)
        assertTrue(repository.removedFromFavourite.isEmpty())
    }

    @Test
    fun toggleFavouriteOnUnknownArticleIsSilentNoOp() = runTest(dispatcher) {
        val repository = FakeNewsRepositoryApi()
        val viewModel = NewsListViewModel(repository)
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onToggleFavourite(article("unknown-id", isFavorite = false))
        advanceUntilIdle()

        assertTrue(repository.addedToFavourite.isEmpty())
        assertTrue(repository.removedFromFavourite.isEmpty())
    }
}
