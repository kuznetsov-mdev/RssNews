package org.kuznetsov.rssnews.data.repository

import org.kuznetsov.rssnews.data.remote.NewsDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class NewsMapperTest {

    @Test
    fun mapsAllFieldsFromFullDto() {
        val dto = NewsDto(
            id = "id-1",
            title = "Title",
            link = "https://example.com/1",
            description = "desc",
            publishedAt = "2026-01-01 00:00:00",
            imageUrl = "https://example.com/img.png",
            sourceName = "Source",
            creator = listOf("Author"),
            category = listOf("politics")
        )

        val state = dto.toDomain()

        assertEquals("id-1", state.id.id)
        assertEquals("Title", state.title.title)
        assertEquals("politics", state.topic.topic)
        assertEquals("Author", state.author.name)
        assertEquals("https://example.com/img.png", state.previewUrl?.url)
        assertFalse(state.isFavourite)
    }

    @Test
    fun fallsBackToSourceNameWhenCreatorMissing() {
        val dto = NewsDto(
            id = "id-2",
            title = "Title",
            link = "https://example.com/2",
            sourceName = "Source",
            creator = null
        )

        val state = dto.toDomain()

        assertEquals("Source", state.author.name)
    }

    @Test
    fun usesEmptyTopicWhenCategoryMissing() {
        val dto = NewsDto(
            id = "id-3",
            title = "Title",
            link = "https://example.com/3",
            category = null
        )

        val state = dto.toDomain()

        assertEquals("", state.topic.topic)
    }

    @Test
    fun previewUrlIsNullWhenImageUrlMissing() {
        val dto = NewsDto(
            id = "id-4",
            title = "Title",
            link = "https://example.com/4",
            imageUrl = null
        )

        val state = dto.toDomain()

        assertNull(state.previewUrl?.url)
    }
}
