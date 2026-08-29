package org.kuznetsov.rssnews.presentation.screen.newslist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview
import org.kuznetsov.rssnews.presentation.common.components.Kicker
import org.kuznetsov.rssnews.presentation.common.components.NewsCard
import org.kuznetsov.rssnews.presentation.common.components.RssDivider
import org.kuznetsov.rssnews.presentation.common.components.ScreenTitle
import org.kuznetsov.rssnews.presentation.model.ArticleUi
import org.kuznetsov.rssnews.presentation.screen.ScreenPreview
import org.kuznetsov.rssnews.presentation.screen.sampleArticles

/** The "Today" news list: a dated masthead over the day's stories. */
@Composable
fun NewsListScreen(
    date: String,
    articles: List<ArticleUi>,
    onArticleClick: (ArticleUi) -> Unit,
    onToggleFavorite: (ArticleUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Kicker(text = date)
                ScreenTitle(text = "Today", modifier = Modifier.padding(top = 4.dp))
            }
            RssDivider()
        }
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

@LightDarkPreview
@Composable
private fun NewsListScreenPreview() {
    ScreenPreview {
        NewsListScreen(
            date = "Friday, 28 August 2026",
            articles = sampleArticles,
            onArticleClick = {},
            onToggleFavorite = {},
        )
    }
}
