package org.kuznetsov.rssnews.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private const val RESPONSE_JSON = """
{
  "status": "success",
  "totalResults": 1,
  "results": [
    {
      "article_id": "id-1",
      "title": "Title 1",
      "link": "https://example.com/1",
      "description": "desc",
      "pubDate": "2026-01-01 00:00:00",
      "image_url": "https://example.com/img1.png",
      "source_name": "Source1",
      "creator": ["Author1"],
      "category": ["politics"]
    }
  ],
  "nextPage": "abc"
}
"""

class NewsApiClientTest {

    private fun apiClient(captured: MutableList<HttpRequestData> = mutableListOf()): RssNewsApiClient {
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
        return RssNewsApiClient(httpClient)
    }

    @Test
    fun fetchNewsParsesResponseBody() = runTest {
        val response = apiClient().fetchNews()

        assertEquals("success", response.status)
        assertEquals(1, response.totalResults)
        assertEquals("abc", response.nextPage)
        assertEquals("id-1", response.results.single().id)
    }

    @Test
    fun fetchNewsWithoutQueryOmitsQParameter() = runTest {
        val captured = mutableListOf<HttpRequestData>()

        apiClient(captured).fetchNews()

        val request = captured.single()
        assertNull(request.url.parameters["q"])
        assertEquals("ru", request.url.parameters["country"])
        assertEquals("ru", request.url.parameters["language"])
    }

    @Test
    fun fetchNewsWithQuerySendsQParameter() = runTest {
        val captured = mutableListOf<HttpRequestData>()

        apiClient(captured).fetchNews(query = "spacex")

        assertEquals("spacex", captured.single().url.parameters["q"])
    }

    @Test
    fun fetchNewsWithBlankQueryOmitsQParameter() = runTest {
        val captured = mutableListOf<HttpRequestData>()

        apiClient(captured).fetchNews(query = "   ")

        assertNull(captured.single().url.parameters["q"])
    }

    @Test
    fun fetchNewsWithoutPageOmitsPageParameter() = runTest {
        val captured = mutableListOf<HttpRequestData>()

        apiClient(captured).fetchNews()

        assertNull(captured.single().url.parameters["page"])
    }

    @Test
    fun fetchNewsWithPageSendsPageParameter() = runTest {
        val captured = mutableListOf<HttpRequestData>()

        apiClient(captured).fetchNews(page = "cursor-2")

        assertEquals("cursor-2", captured.single().url.parameters["page"])
    }

    @Test
    fun fetchNewsWithBlankPageOmitsPageParameter() = runTest {
        val captured = mutableListOf<HttpRequestData>()

        apiClient(captured).fetchNews(page = "   ")

        assertNull(captured.single().url.parameters["page"])
    }
}
