package org.kuznetsov.rssnews.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(news: NewsEntity)

    @Query("SELECT * FROM news")
    fun getAll(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getById(newsId: String): NewsEntity

    @Query("SELECT * FROM news WHERE title LIKE '%' || :title || '%'")
    fun getByTitle(title: String): Flow<List<NewsEntity>>

    @Delete
    suspend fun delete(news: NewsEntity)

    @Query("DELETE FROM news WHERE id = :newsId")
    suspend fun deleteById(newsId: String)
}