package org.kuznetsov.rssnews.domain.api

import kotlinx.coroutines.flow.Flow
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsPage
import org.kuznetsov.rssnews.domain.model.NewsState

interface NewsRepositoryApi {

    fun findAll(page: String? = null): Flow<NewsPage>

    fun findByQuery(query: String, page: String? = null): Flow<NewsPage>

    suspend fun addToFavourite(news: NewsState)

    suspend fun removeFromFavourite(newsId: NewsId)

    suspend fun shareNews(news: NewsState)
}