package org.kuznetsov.rssnews.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    // A one-shot fetch: it must NOT keep observing newsDao, or a favourite toggle
    // elsewhere would re-emit this same page and wipe out pages appended by loadMore().
    // Favourite status for already-loaded articles is instead kept live at the
    // presentation layer, combined from getFavourites().
    private fun newsFlow(query: String?, page: String?): Flow<NewsPage> = flow {
        val response = apiClient.fetchNews(query, page)
        val favouriteIds = newsDao.getAll().first().map { NewsId(it.id) }.toSet()
        val items = response.results.map { it.toDomain().let { news -> news.copy(isFavourite = news.id in favouriteIds) } }
        emit(NewsPage(items, response.nextPage))
    }
}
