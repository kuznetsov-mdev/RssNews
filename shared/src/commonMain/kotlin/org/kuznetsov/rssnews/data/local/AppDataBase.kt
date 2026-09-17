package org.kuznetsov.rssnews.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

const val DB_NAME = "rss_news.db"

expect fun getDataBaseBuilder(): RoomDatabase.Builder<AppDataBase>

@Database(entities = [NewsEntity::class], version = 2)
@ConstructedBy(AppDataBaseConstructor::class)
abstract class AppDataBase : RoomDatabase() {
    abstract  fun newsDao(): NewsDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDataBaseConstructor : RoomDatabaseConstructor<AppDataBase> {
    override fun initialize(): AppDataBase
}