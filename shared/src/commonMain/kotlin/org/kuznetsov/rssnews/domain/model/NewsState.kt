package org.kuznetsov.rssnews.domain.model

data class NewsState(
    val id: NewsId,
    val title: NewsTitle,
    val topic: NewsTopic,
    val author: NewsAuthor,
    val previewUrl: PreviewUrl?,
    val body: String = "",
    val sourceUrl: String = "",
    val isFavourite: Boolean = false
)