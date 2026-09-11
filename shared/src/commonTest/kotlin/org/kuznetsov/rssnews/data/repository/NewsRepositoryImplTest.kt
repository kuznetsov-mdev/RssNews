package org.kuznetsov.rssnews.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.kuznetsov.rssnews.data.local.NewsDao
import org.kuznetsov.rssnews.data.local.NewsEntity
import org.kuznetsov.rssnews.data.remote.RssNewsApiClient
import org.kuznetsov.rssnews.domain.model.NewsId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private class FakeNewsDao : NewsDao {
    private val entities = MutableStateFlow<Map<String, NewsEntity>>(emptyMap())

    override suspend fun insert(news: NewsEntity) {
        entities.update { it + (news.id to news) }
    }

    override fun getAll(): Flow<List<NewsEntity>> = entities.map { it.values.toList() }

    override suspend fun getById(newsId: String): NewsEntity = entities.value.getValue(newsId)

    override fun getByTitle(title: String): Flow<List<NewsEntity>> =
        entities.map { map -> map.values.filter { it.title.contains(title) } }

    override suspend fun delete(news: NewsEntity) {
        entities.update { it - news.id }
    }

    override suspend fun deleteById(newsId: String) {
        entities.update { it - newsId }
    }
}

private const val RESPONSE_JSON = """
{
  "status": "success",
  "totalResults": 2,
  "results": [
    {
      "article_id": "id-1",
      "title": "Title 1",
      "link": "https://example.com/1",
      "source_name": "Source1",
      "category": ["politics"]
    },
    {
      "article_id": "id-2",
      "title": "Title 2",
      "link": "https://example.com/2",
      "source_name": "Source2",
      "category": ["sport"]
    }
  ],
  "nextPage": "cursor-2"
}
"""

class NewsRepositoryImplTest {

    private fun repository(captured: MutableList<HttpRequestData> = mutableListOf()): NewsRepositoryImpl {
        val engine = MockEngine { request ->
            captured += request
            respond(
                content = RESPONSE_JSON,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val httpClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        return NewsRepositoryImpl(RssNewsApiClient(httpClient), FakeNewsDao())
    }

    @Test
    fun findAllReturnsMappedNewsNoneFavourite() = runTest {
        val page = repository().findAll().first()

        assertEquals(listOf("id-1", "id-2"), page.items.map { it.id.id })
        assertTrue(page.items.none { it.isFavourite })
    }

    @Test
    fun findAllExposesNextPageCursor() = runTest {
        val page = repository().findAll().first()

        assertEquals("cursor-2", page.nextPage)
    }

    @Test
    fun findAllForwardsPageParameterToApi() = runTest {
        val captured = mutableListOf<HttpRequestData>()

        repository(captured).findAll(page = "cursor-1").first()

        assertEquals("cursor-1", captured.single().url.parameters["page"])
    }

    @Test
    fun findAllWithoutPageOmitsPageParameter() = runTest {
        val captured = mutableListOf<HttpRequestData>()

        repository(captured).findAll().first()

        assertNull(captured.single().url.parameters["page"])
    }

    @Test
    fun findByQuerySendsQAndPageParametersToApi() = runTest {
        val captured = mutableListOf<HttpRequestData>()

        repository(captured).findByQuery("spacex", page = "cursor-1").first()

        assertEquals("spacex", captured.single().url.parameters["q"])
        assertEquals("cursor-1", captured.single().url.parameters["page"])
    }

    @Test
    fun addToFavouriteMarksMatchingNewsAsFavourite() = runTest {
        val repo = repository()

        val page = repo.findAll().first()
        repo.addToFavourite(page.items.first { it.id.id == "id-1" })

        val updated = repo.findAll().first()
        assertTrue(updated.items.single { it.id.id == "id-1" }.isFavourite)
        assertFalse(updated.items.single { it.id.id == "id-2" }.isFavourite)
    }

    @Test
    fun removeFromFavouriteClearsFavouriteFlag() = runTest {
        val repo = repository()

        val page = repo.findAll().first()
        repo.addToFavourite(page.items.first { it.id.id == "id-1" })
        repo.removeFromFavourite(NewsId("id-1"))

        val updated = repo.findAll().first()
        assertFalse(updated.items.single { it.id.id == "id-1" }.isFavourite)
    }
}
