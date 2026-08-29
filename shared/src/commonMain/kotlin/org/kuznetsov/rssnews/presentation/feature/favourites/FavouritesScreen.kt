package org.kuznetsov.rssnews.presentation.feature.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview
import org.kuznetsov.rssnews.presentation.common.components.Byline
import org.kuznetsov.rssnews.presentation.common.components.Kicker
import org.kuznetsov.rssnews.presentation.common.components.NewsCard
import org.kuznetsov.rssnews.presentation.common.components.RssDivider
import org.kuznetsov.rssnews.presentation.common.components.ScreenTitle
import org.kuznetsov.rssnews.presentation.model.ArticleUi
import org.kuznetsov.rssnews.presentation.feature.ScreenPreview
import org.kuznetsov.rssnews.presentation.feature.sampleArticles

/** The favourites tab: every story the reader has hearted, newest first. */
@Composable
fun FavouritesScreen(
    articles: List<ArticleUi>,
    onArticleClick: (ArticleUi) -> Unit,
    onToggleFavorite: (ArticleUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Kicker(text = savedCountLabel(articles.size))
                ScreenTitle(text = "Favourites", modifier = Modifier.padding(top = 4.dp))
            }
            RssDivider()
        }
        if (articles.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Byline(text = "No favourites yet.")
                }
            }
        } else {
            items(articles, key = { it.id }) { article ->
                NewsCard(
                    category = article.category,
                    headline = article.headline,
                    byline = article.byline,
                    isFavorite = article.isFavorite,
                    onToggleFavorite = { onToggleFavorite(article) },
                    onClick = { onArticleClick(article) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                RssDivider()
            }
        }
    }
}

private fun savedCountLabel(count: Int): String = if (count == 1) "1 story saved" else "$count stories saved"

@LightDarkPreview
@Composable
private fun FavouritesScreenPreview() {
    ScreenPreview {
        FavouritesScreen(
            articles = sampleArticles.filter { it.isFavorite },
            onArticleClick = {},
            onToggleFavorite = {},
        )
    }
}

@LightDarkPreview
@Composable
private fun FavouritesScreenEmptyPreview() {
    ScreenPreview {
        FavouritesScreen(
            articles = emptyList(),
            onArticleClick = {},
            onToggleFavorite = {},
        )
    }
}
