package org.kuznetsov.rssnews.presentation.feature

import org.kuznetsov.rssnews.presentation.model.ArticleUi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun article(
    id: String,
    headline: String = "",
    category: String = "",
    byline: String = "",
) = ArticleUi(id = id, category = category, headline = headline, byline = byline, isFavorite = false)

class ArticleSearchTest {

    @Test
    fun blankQueryReturnsListUnchanged() {
        val articles = listOf(article("1", headline = "Foo"), article("2", headline = "Bar"))

        assertEquals(articles, articles.filterByQuery("   "))
    }

    @Test
    fun matchesByHeadline() {
        val target = article("1", headline = "Kotlin Multiplatform")
        val other = article("2", headline = "Something else")

        assertEquals(listOf(target), listOf(target, other).filterByQuery("kotlin"))
    }

    @Test
    fun matchesByCategory() {
        val target = article("1", category = "Technology")
        val other = article("2", category = "Sports")

        assertEquals(listOf(target), listOf(target, other).filterByQuery("tech"))
    }

    @Test
    fun matchesByByline() {
        val target = article("1", byline = "Jane Doe")
        val other = article("2", byline = "John Smith")

        assertEquals(listOf(target), listOf(target, other).filterByQuery("jane"))
    }

    @Test
    fun noMatchReturnsEmptyList() {
        val articles = listOf(article("1", headline = "Foo"), article("2", headline = "Bar"))

        assertTrue(articles.filterByQuery("nonexistent").isEmpty())
    }

    @Test
    fun matchIsCaseInsensitive() {
        val target = article("1", headline = "KOTLIN")

        assertEquals(listOf(target), listOf(target).filterByQuery("kotlin"))
    }

    @Test
    fun emptyListReturnsEmptyList() {
        assertTrue(emptyList<ArticleUi>().filterByQuery("anything").isEmpty())
    }
}
