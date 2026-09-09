package org.kuznetsov.rssnews.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsPage

class GetNewsByQueryUseCase(
    private val repository: NewsRepositoryApi,
    private val query: String
) {
    operator fun invoke(page: String? = null): Flow<NewsPage> = repository.findByQuery(query, page)
}