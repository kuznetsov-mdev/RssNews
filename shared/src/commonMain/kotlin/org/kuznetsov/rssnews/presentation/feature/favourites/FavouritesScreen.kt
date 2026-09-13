package org.kuznetsov.rssnews.presentation.feature.favourites

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
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
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
import rssnews.shared.generated.resources.Res
import rssnews.shared.generated.resources.empty_favourites
import rssnews.shared.generated.resources.empty_search_no_results
import rssnews.shared.generated.resources.saved_stories_count

/** The favourites tab: a search field over every story the reader has hearted. */
@Composable
fun FavouritesScreen(
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
                Kicker(text = pluralStringResource(Res.plurals.saved_stories_count, articles.size, articles.size))
                ScreenTitle(text = "Favourites", modifier = Modifier.padding(top = 4.dp))
                RssSearchField(
                    query = query,
                    onQueryChange = { query = it },
                    placeholder = "Search saved stories",
                    modifier = Modifier.padding(top = 16.dp),
                )
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
                    Byline(text = stringResource(Res.string.empty_favourites))
                }
            }
        } else if (visibleArticles.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Byline(text = stringResource(Res.string.empty_search_no_results))
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
