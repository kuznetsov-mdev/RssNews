package org.kuznetsov.rssnews.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsAuthor
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsPage
import org.kuznetsov.rssnews.domain.model.NewsState
import org.kuznetsov.rssnews.domain.model.NewsTitle
import org.kuznetsov.rssnews.domain.model.NewsTopic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class FakeNewsRepositoryApi(
    private val page: NewsPage = NewsPage(items = emptyList()),
    private val favourites: Flow<List<NewsState>> = flowOf(emptyList()),
) : NewsRepositoryApi {

    var lastFindAllPage: String? = null
        private set
    var findAllCalls = 0
        private set
    var lastFindByQueryArgs: Pair<String, String?>? = null
        private set
    var lastAddedToFavourite: NewsState? = null
        private set
    var lastRemovedFromFavourite: NewsId? = null
        private set
    var lastSharedNews: NewsState? = null
        private set

    override fun findAll(page: String?): Flow<NewsPage> {
        findAllCalls++
        lastFindAllPage = page
        return flowOf(this.page)
    }

    override fun findByQuery(query: String, page: String?): Flow<NewsPage> {
        lastFindByQueryArgs = query to page
        return flowOf(this.page)
    }

    override fun getFavourites(): Flow<List<NewsState>> = favourites

    override suspend fun addToFavourite(news: NewsState) {
        lastAddedToFavourite = news
    }

    override suspend fun removeFromFavourite(newsId: NewsId) {
        lastRemovedFromFavourite = newsId
    }

    override suspend fun shareNews(news: NewsState) {
        lastSharedNews = news
    }
}

internal fun sampleNewsState(id: String = "id-1") = NewsState(
    id = NewsId(id),
    title = NewsTitle("Title"),
    topic = NewsTopic("Topic"),
    author = NewsAuthor("Author"),
    previewUrl = null,
)

class UseCaseDelegationTest {

    @Test
    fun fetchNewsUseCaseWithoutPageForwardsNullAndReturnsRepositoryResult() = runTest {
        val page = NewsPage(items = listOf(sampleNewsState()), nextPage = "next")
        val repository = FakeNewsRepositoryApi(page = page)

        val result = FetchNewsUseCase(repository)().first()

        assertNull(repository.lastFindAllPage)
        assertEquals(1, repository.findAllCalls)
        assertEquals(page, result)
    }

    @Test
    fun fetchNewsUseCaseForwardsExplicitPage() = runTest {
        val repository = FakeNewsRepositoryApi()

        FetchNewsUseCase(repository)(page = "cursor-1").first()

        assertEquals("cursor-1", repository.lastFindAllPage)
    }

    @Test
    fun getNewsByQueryUseCaseForwardsQueryAndNullPage() = runTest {
        val repository = FakeNewsRepositoryApi()

        GetNewsByQueryUseCase(repository, query = "spacex")().first()

        assertEquals("spacex" to null, repository.lastFindByQueryArgs)
    }

    @Test
    fun getNewsByQueryUseCaseForwardsQueryAndExplicitPage() = runTest {
        val repository = FakeNewsRepositoryApi()

        GetNewsByQueryUseCase(repository, query = "spacex")(page = "cursor-1").first()

        assertEquals("spacex" to "cursor-1", repository.lastFindByQueryArgs)
    }

    @Test
    fun addNewsToFavouriteUseCaseDelegatesToRepository() = runTest {
        val repository = FakeNewsRepositoryApi()
        val news = sampleNewsState()

        AddNewsToFavouriteUseCase(repository, news)()

        assertEquals(news, repository.lastAddedToFavourite)
    }

    @Test
    fun removeNewsFromFavouriteUseCaseDelegatesToRepository() = runTest {
        val repository = FakeNewsRepositoryApi()
        val newsId = NewsId("id-1")

        RemoveNewsFromFavouriteUseCase(repository, newsId)()

        assertEquals(newsId, repository.lastRemovedFromFavourite)
    }

    @Test
    fun shareNewsUseCaseDelegatesToRepository() = runTest {
        val repository = FakeNewsRepositoryApi()
        val news = sampleNewsState()

        ShareNewsUseCase(repository, news)()

        assertEquals(news, repository.lastSharedNews)
    }
}
