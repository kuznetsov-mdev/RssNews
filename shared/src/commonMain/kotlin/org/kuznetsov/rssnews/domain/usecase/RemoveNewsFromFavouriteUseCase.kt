package org.kuznetsov.rssnews.domain.usecase

import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsId

class RemoveNewsFromFavouriteUseCase(
    private val repository: NewsRepositoryApi,
    private val newsId: NewsId
) {
    suspend operator fun invoke(): Unit = repository.removeFromFavourite(newsId)
}