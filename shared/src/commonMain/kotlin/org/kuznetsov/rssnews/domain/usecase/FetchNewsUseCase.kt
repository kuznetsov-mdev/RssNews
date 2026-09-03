package org.kuznetsov.rssnews.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsState

class FetchNewsUseCase(private val repository: NewsRepositoryApi) {
    operator fun invoke(): Flow<List<NewsState>> = repository.findAll()
}