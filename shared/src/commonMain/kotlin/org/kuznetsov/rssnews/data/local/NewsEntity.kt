package org.kuznetsov.rssnews.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val topic: String,
    val author: String,
    val previewUrl: String?,
    val body: String = "",
    val sourceUrl: String = "",
    val createdAt: Long
)
