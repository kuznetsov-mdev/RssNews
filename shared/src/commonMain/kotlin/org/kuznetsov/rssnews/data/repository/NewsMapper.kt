package org.kuznetsov.rssnews.data.repository

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.kuznetsov.rssnews.data.local.NewsEntity
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

fun NewsEntity.toDomain(): NewsState = NewsState(
    id = NewsId(id),
    title = NewsTitle(title),
    topic = NewsTopic(topic),
    author = NewsAuthor(author),
    previewUrl = PreviewUrl(previewUrl)
)

@OptIn(ExperimentalTime::class)
fun NewsState.toEntity(createdAt: Long = Clock.System.now().toEpochMilliseconds()): NewsEntity = NewsEntity(
    id = id.id,
    title = title.title,
    topic = topic.topic,
    author = author.name,
    previewUrl = previewUrl?.url,
    createdAt = createdAt
)
