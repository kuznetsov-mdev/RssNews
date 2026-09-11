package org.kuznetsov.rssnews.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import org.kuznetsov.rssnews.RssNewsApp

actual fun getDataBaseBuilder(): RoomDatabase.Builder<AppDataBase> {
    val appContext = RssNewsApp.appContext
    val dbFile = appContext.getDatabasePath("rss_news.db")

    return Room.databaseBuilder<AppDataBase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}