package org.kuznetsov.rssnews.presentation.model

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

class ArticleUiMapperTest {

    @Test
    fun mapsAllFieldsWhenPreviewUrlPresent() {
        val news = NewsState(
            id = NewsId("id-1"),
            title = NewsTitle("Headline"),
            topic = NewsTopic("Tech"),
            author = NewsAuthor("Jane Doe"),
            previewUrl = PreviewUrl("https://example.com/img.png"),
            isFavourite = true,
        )

        val ui = news.toArticleUi()

        assertEquals("id-1", ui.id)
        assertEquals("Tech", ui.category)
        assertEquals("Headline", ui.headline)
        assertEquals("Jane Doe", ui.byline)
        assertEquals(true, ui.isFavorite)
        assertEquals("https://example.com/img.png", ui.previewUrl)
    }

    @Test
    fun nullPreviewUrlMapsToNull() {
        val news = NewsState(
            id = NewsId("id-1"),
            title = NewsTitle("Headline"),
            topic = NewsTopic("Tech"),
            author = NewsAuthor("Jane Doe"),
            previewUrl = null,
        )

        assertNull(news.toArticleUi().previewUrl)
    }

    @Test
    fun previewUrlWrapperWithNullInnerUrlMapsToNull() {
        val news = NewsState(
            id = NewsId("id-1"),
            title = NewsTitle("Headline"),
            topic = NewsTopic("Tech"),
            author = NewsAuthor("Jane Doe"),
            previewUrl = PreviewUrl(null),
        )

        assertNull(news.toArticleUi().previewUrl)
    }

    @Test
    fun isFavouriteFalsePropagatesToIsFavoriteFalse() {
        val news = NewsState(
            id = NewsId("id-1"),
            title = NewsTitle("Headline"),
            topic = NewsTopic("Tech"),
            author = NewsAuthor("Jane Doe"),
            previewUrl = null,
            isFavourite = false,
        )

        assertFalse(news.toArticleUi().isFavorite)
    }
}
