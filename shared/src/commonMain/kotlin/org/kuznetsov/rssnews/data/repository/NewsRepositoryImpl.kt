package org.kuznetsov.rssnews.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.kuznetsov.rssnews.data.local.NewsDao
import org.kuznetsov.rssnews.data.remote.RssNewsApiClient
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsPage
import org.kuznetsov.rssnews.domain.model.NewsState

class NewsRepositoryImpl(
    private val apiClient: RssNewsApiClient,
    private val newsDao: NewsDao
) : NewsRepositoryApi {

    override fun findAll(page: String?): Flow<NewsPage> = newsFlow(query = null, page = page)

    override fun findByQuery(query: String, page: String?): Flow<NewsPage> = newsFlow(query, page)

    override fun getFavourites(): Flow<List<NewsState>> =
        newsDao.getAll().map { entities -> entities.map { it.toDomain().copy(isFavourite = true) } }

    override suspend fun addToFavourite(news: NewsState) {
        newsDao.insert(news.toEntity())
    }

    override suspend fun removeFromFavourite(newsId: NewsId) {
        newsDao.deleteById(newsId.id)
    }

    override suspend fun shareNews(news: NewsState) {
        TODO("Platform share sheet is not wired up yet")
    }

    private fun newsFlow(query: String?, page: String?): Flow<NewsPage> {
        val news = flow {
            val response = apiClient.fetchNews(query, page)
            emit(NewsPage(response.results.map { it.toDomain() }, response.nextPage))
        }
        val favouriteIds = newsDao.getAll().map { entities -> entities.map { NewsId(it.id) }.toSet() }
        return news.combine(favouriteIds) { newsPage, favourites ->
            newsPage.copy(items = newsPage.items.map { it.copy(isFavourite = it.id in favourites) })
        }
    }
}
