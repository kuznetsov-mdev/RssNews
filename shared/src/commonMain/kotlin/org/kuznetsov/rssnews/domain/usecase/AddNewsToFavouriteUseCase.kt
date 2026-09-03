package org.kuznetsov.rssnews.domain.usecase

import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsState

class AddNewsToFavouriteUseCase(
    private val repository: NewsRepositoryApi,
    private val newsState: NewsState
) {
    suspend operator fun invoke(): Unit = repository.addToFavourite(newsState)
}