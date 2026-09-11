package org.kuznetsov.rssnews.di

import org.koin.dsl.module
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
    single<NewsRepositoryApi> { NewsRepositoryImpl(get(), get()) }

    factory { FetchNewsUseCase(get()) }
    factory { (query: String) -> GetNewsByQueryUseCase(get(), query) }
    factory { (newsState: NewsState) -> AddNewsToFavouriteUseCase(get(), newsState) }
    factory { (newsId: NewsId) -> RemoveNewsFromFavouriteUseCase(get(), newsId) }
    factory { (newsState: NewsState) -> ShareNewsUseCase(get(), newsState) }
}
