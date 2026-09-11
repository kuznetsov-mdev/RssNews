package org.kuznetsov.rssnews.di

import org.koin.dsl.module
import org.kuznetsov.rssnews.data.local.NewsDao
import org.kuznetsov.rssnews.data.remote.RssNewsApiClient
import org.kuznetsov.rssnews.data.repository.NewsRepositoryImpl
import org.kuznetsov.rssnews.domain.api.NewsRepositoryApi
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsState
import org.kuznetsov.rssnews.domain.usecase.AddNewsToFavouriteUseCase
import org.kuznetsov.rssnews.domain.usecase.FetchNewsUseCase
import org.kuznetsov.rssnews.domain.usecase.GetNewsByQueryUseCase
import org.kuznetsov.rssnews.domain.usecase.RemoveNewsFromFavouriteUseCase
import org.kuznetsov.rssnews.domain.usecase.ShareNewsUseCase

val domainModule = module {
    single<NewsRepositoryApi> {
        NewsRepositoryImpl(
            apiClient = get<RssNewsApiClient>(),
            newsDao = get<NewsDao>()
        )
    }

    factory { FetchNewsUseCase(repository = get<NewsRepositoryApi>()) }
    factory { (query: String) -> GetNewsByQueryUseCase(repository = get(), query) }
    factory { (newsState: NewsState) -> AddNewsToFavouriteUseCase(repository = get<NewsRepositoryApi>(), newsState) }
    factory { (newsId: NewsId) -> RemoveNewsFromFavouriteUseCase(repository = get<NewsRepositoryApi>(), newsId) }
    factory { (newsState: NewsState) -> ShareNewsUseCase(repository = get<NewsRepositoryApi>(), newsState) }
}
