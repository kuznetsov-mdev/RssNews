package org.kuznetsov.rssnews.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsPage

class FetchNewsUseCase(private val repository: NewsRepositoryApi) {
    operator fun invoke(page: String? = null): Flow<NewsPage> = repository.findAll(page)
}