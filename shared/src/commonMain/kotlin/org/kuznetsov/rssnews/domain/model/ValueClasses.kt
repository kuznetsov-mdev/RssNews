package org.kuznetsov.rssnews.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class NewsId(val id: String)

@JvmInline
value class NewsTopic(val topic: String) //todo:possibly enum or sealed class

@JvmInline
value class NewsTitle(val title: String)

@JvmInline
value class NewsAuthor(val name: String)

@JvmInline
value class PreviewUrl(val url: String?)
