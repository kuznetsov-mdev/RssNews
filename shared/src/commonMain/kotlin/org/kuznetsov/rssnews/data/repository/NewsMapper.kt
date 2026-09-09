package org.kuznetsov.rssnews.data.repository

import org.kuznetsov.rssnews.data.remote.NewsDto
import org.kuznetsov.rssnews.domain.model.NewsAuthor
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsState
import org.kuznetsov.rssnews.domain.model.NewsTitle
import org.kuznetsov.rssnews.domain.model.NewsTopic
import org.kuznetsov.rssnews.domain.model.PreviewUrl

fun NewsDto.toDomain(): NewsState = NewsState(
    id = NewsId(id),
    title = NewsTitle(title),
    topic = NewsTopic(category?.firstOrNull().orEmpty()),
    author = NewsAuthor(creator?.firstOrNull() ?: sourceName.orEmpty()),
    previewUrl = PreviewUrl(imageUrl)
)
