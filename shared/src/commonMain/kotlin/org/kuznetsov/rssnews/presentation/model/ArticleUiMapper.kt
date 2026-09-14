package org.kuznetsov.rssnews.presentation.model

import org.kuznetsov.rssnews.domain.model.NewsState

fun NewsState.toArticleUi(): ArticleUi = ArticleUi(
    id = id.id,
    category = topic.topic,
    headline = title.title,
    byline = author.name,
    isFavorite = isFavourite,
    previewUrl = previewUrl?.url,
)
