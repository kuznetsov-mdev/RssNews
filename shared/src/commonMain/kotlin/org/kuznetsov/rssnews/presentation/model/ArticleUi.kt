package org.kuznetsov.rssnews.presentation.model

/** The presentation-layer shape of a story, shared by every screen. */
data class ArticleUi(
    val id: String,
    val category: String,
    val headline: String,
    val byline: String,
    val isFavorite: Boolean,
    val body: String = "",
    val previewUrl: String? = null,
    val sourceUrl: String = "",
)
