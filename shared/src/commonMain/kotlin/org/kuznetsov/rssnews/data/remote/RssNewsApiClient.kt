package org.kuznetsov.rssnews.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.kuznetsov.rssnews.ApiKeys

class RssNewsApiClient(private val httpClient: HttpClient) {

    suspend fun fetchNews(query: String? = null, page: String? = null): NewsDataResponseDto {
        return httpClient.get("latest") {
            parameter("apikey", ApiKeys.NEWSDATA_API_KEY)
            parameter("country", "ru")
            parameter("language", "ru")
            if (!query.isNullOrBlank()) {
                parameter("q", query)
            }
            if (!page.isNullOrBlank()) {
                parameter("page", page)
            }
        }.body()
    }
}
