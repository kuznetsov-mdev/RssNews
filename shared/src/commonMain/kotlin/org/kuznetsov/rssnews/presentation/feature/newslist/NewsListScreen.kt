package org.kuznetsov.rssnews.presentation.feature.newslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview
import org.kuznetsov.rssnews.presentation.common.components.Byline
import org.kuznetsov.rssnews.presentation.common.components.Kicker
import org.kuznetsov.rssnews.presentation.common.components.NewsCard
import org.kuznetsov.rssnews.presentation.common.components.RssDivider
import org.kuznetsov.rssnews.presentation.common.components.RssSearchField
import org.kuznetsov.rssnews.presentation.common.components.ScreenTitle
import org.kuznetsov.rssnews.presentation.feature.ScreenPreview
import org.kuznetsov.rssnews.presentation.feature.filterByQuery
import org.kuznetsov.rssnews.presentation.feature.sampleArticles
import org.kuznetsov.rssnews.presentation.model.ArticleUi

/** The "Today" news list: a dated masthead, a search field, and the day's stories. */
@Composable
fun NewsListScreen(
    date: String,
    articles: List<ArticleUi>,
    onArticleClick: (ArticleUi) -> Unit,
    onToggleFavorite: (ArticleUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    val visibleArticles = remember(articles, query) { articles.filterByQuery(query) }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Kicker(text = date)
                ScreenTitle(text = "Today", modifier = Modifier.padding(top = 4.dp))
                RssSearchField(
                    query = query,
                    onQueryChange = { query = it },
                    placeholder = "Search today's stories",
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
            RssDivider()
        }
        if (visibleArticles.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Byline(text = "Nothing found.")
                }
            }
        } else {
            items(visibleArticles, key = { it.id }) { article ->
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
