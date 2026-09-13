package org.kuznetsov.rssnews.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.kuznetsov.rssnews.presentation.feature.newslist.NewsListViewModel

val presentationModule = module {
    viewModelOf(::NewsListViewModel)
}
