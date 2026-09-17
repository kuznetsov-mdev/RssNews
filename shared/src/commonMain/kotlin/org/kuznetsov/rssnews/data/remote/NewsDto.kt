package org.kuznetsov.rssnews.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDataResponseDto(
    val status: String,
    val totalResults: Int = 0,
    val results: List<NewsDto> = emptyList(),
    val nextPage: String? = null
)

@Serializable
data class NewsDto(
    @SerialName("article_id")
    val id: String,
    val title: String,
    val link: String,
    val description: String? = null,
    @SerialName("pubDate")
    val publishedAt: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("source_name")
    val sourceName: String? = null,
    val creator: List<String>? = null,
    val category: List<String>? = null
)
