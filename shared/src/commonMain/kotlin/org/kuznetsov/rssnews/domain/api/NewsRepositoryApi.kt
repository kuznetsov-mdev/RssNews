package org.kuznetsov.rssnews.domain.api

import kotlinx.coroutines.flow.Flow
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsState

interface NewsRepositoryApi {

    fun findAll(): Flow<List<NewsState>>

    fun findByQuery(query: String): Flow<List<NewsState>>

    suspend fun addToFavourite(news: NewsState)

    suspend fun removeFromFavourite(newsId: NewsId)

    suspend fun shareNews(news: NewsState)
}