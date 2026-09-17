package org.kuznetsov.rssnews.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import org.kuznetsov.rssnews.data.local.AppDataBase
import org.kuznetsov.rssnews.data.local.NewsDao
import org.kuznetsov.rssnews.data.local.getDataBaseBuilder
import org.kuznetsov.rssnews.data.remote.RssNewsApiClient
import org.kuznetsov.rssnews.data.remote.createHttpClient

val dataModule = module {
    single { createHttpClient() }
    single { RssNewsApiClient(get<HttpClient>()) }

    single<AppDataBase> {
        getDataBaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.Default)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single<NewsDao> { get<AppDataBase>().newsDao() }
}