package org.kuznetsov.rssnews.data.repository

import org.kuznetsov.rssnews.data.local.NewsEntity
import org.kuznetsov.rssnews.data.remote.NewsDto
import org.kuznetsov.rssnews.domain.model.NewsAuthor
import org.kuznetsov.rssnews.domain.model.NewsId
import org.kuznetsov.rssnews.domain.model.NewsState
import org.kuznetsov.rssnews.domain.model.NewsTitle
import org.kuznetsov.rssnews.domain.model.NewsTopic
import org.kuznetsov.rssnews.domain.model.PreviewUrl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
        assertEquals("desc", state.body)
        assertEquals("https://example.com/1", state.sourceUrl)
        assertFalse(state.isFavourite)
    }

    @Test
    fun usesEmptyBodyWhenDescriptionMissing() {
        val dto = NewsDto(
            id = "id-5",
            title = "Title",
            link = "https://example.com/5",
            description = null
        )

        val state = dto.toDomain()

        assertEquals("", state.body)
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

    @Test
    fun entityToDomainMapsAllFields() {
        val entity = NewsEntity(
            id = "id-1",
            title = "Title",
            topic = "politics",
            author = "Author",
            previewUrl = "https://example.com/img.png",
            body = "desc",
            sourceUrl = "https://example.com/1",
            createdAt = 1_000L
        )

        val state = entity.toDomain()

        assertEquals("id-1", state.id.id)
        assertEquals("Title", state.title.title)
        assertEquals("politics", state.topic.topic)
        assertEquals("Author", state.author.name)
        assertEquals("https://example.com/img.png", state.previewUrl?.url)
        assertEquals("desc", state.body)
        assertEquals("https://example.com/1", state.sourceUrl)
        assertFalse(state.isFavourite)
    }

    @Test
    fun entityToDomainPreservesNullPreviewUrl() {
        val entity = NewsEntity(
            id = "id-2",
            title = "Title",
            topic = "politics",
            author = "Author",
            previewUrl = null,
            createdAt = 1_000L
        )

        val state = entity.toDomain()

        assertNull(state.previewUrl?.url)
    }

    @Test
    fun domainToEntityMapsAllFieldsWithExplicitCreatedAt() {
        val state = NewsState(
            id = NewsId("id-1"),
            title = NewsTitle("Title"),
            topic = NewsTopic("politics"),
            author = NewsAuthor("Author"),
            previewUrl = PreviewUrl("https://example.com/img.png"),
            body = "desc",
            sourceUrl = "https://example.com/1",
            isFavourite = true
        )

        val entity = state.toEntity(createdAt = 42L)

        assertEquals("id-1", entity.id)
        assertEquals("Title", entity.title)
        assertEquals("politics", entity.topic)
        assertEquals("Author", entity.author)
        assertEquals("https://example.com/img.png", entity.previewUrl)
        assertEquals("desc", entity.body)
        assertEquals("https://example.com/1", entity.sourceUrl)
        assertEquals(42L, entity.createdAt)
    }

    @Test
    fun domainToEntityPreservesNullPreviewUrl() {
        val state = NewsState(
            id = NewsId("id-2"),
            title = NewsTitle("Title"),
            topic = NewsTopic("politics"),
            author = NewsAuthor("Author"),
            previewUrl = null
        )

        val entity = state.toEntity(createdAt = 42L)

        assertNull(entity.previewUrl)
    }

    @Test
    fun domainToEntityDefaultsCreatedAtToCurrentTime() {
        val state = NewsState(
            id = NewsId("id-3"),
            title = NewsTitle("Title"),
            topic = NewsTopic("politics"),
            author = NewsAuthor("Author"),
            previewUrl = null
        )

        val entity = state.toEntity()

        assertTrue(entity.createdAt > 0L)
    }
}
