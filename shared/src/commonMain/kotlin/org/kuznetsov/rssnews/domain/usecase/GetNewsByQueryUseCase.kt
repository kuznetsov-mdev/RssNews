package org.kuznetsov.rssnews.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsState

class GetNewsByQueryUseCase(
    private val repository: NewsRepositoryApi,
    private val query: String
) {
    operator fun invoke(): Flow<List<NewsState>> = repository.findByQuery(query)
}