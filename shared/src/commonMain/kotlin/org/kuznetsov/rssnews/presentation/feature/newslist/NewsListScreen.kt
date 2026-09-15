package org.kuznetsov.rssnews.presentation.feature.newslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview
import org.kuznetsov.rssnews.presentation.common.components.Byline
import org.kuznetsov.rssnews.presentation.common.components.Kicker
import org.kuznetsov.rssnews.presentation.common.components.NewsCard
import org.kuznetsov.rssnews.presentation.common.components.RssDivider
import org.kuznetsov.rssnews.presentation.common.components.RssSearchField
import org.kuznetsov.rssnews.presentation.common.components.ScreenTitle
import org.kuznetsov.rssnews.presentation.feature.ScreenPreview
import org.kuznetsov.rssnews.presentation.feature.sampleArticles
import org.kuznetsov.rssnews.presentation.model.ArticleUi
import rssnews.shared.generated.resources.Res
import rssnews.shared.generated.resources.empty_search_no_results
import rssnews.shared.generated.resources.news_list_search_placeholder
import rssnews.shared.generated.resources.news_list_title

private const val LOAD_MORE_THRESHOLD = 3

/** The "Today" news list: a dated masthead, a search field, and the day's stories. */
@Composable
fun NewsListScreen(
    date: String,
    query: String,
    onQueryChange: (String) -> Unit,
    articles: List<ArticleUi>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onArticleClick: (ArticleUi) -> Unit,
    onToggleFavorite: (ArticleUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            layoutInfo.totalItemsCount > 0 && lastVisibleIndex >= layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
        }
    }
    LaunchedEffect(shouldLoadMore, articles) {
        if (shouldLoadMore && articles.isNotEmpty()) {
            onLoadMore()
        }
    }

    LazyColumn(state = listState, modifier = modifier.fillMaxSize()) {
        item {
            Column {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ScreenTitle(text = stringResource(Res.string.news_list_title), modifier = Modifier.weight(1f))
                        Kicker(text = date)
                    }
                    RssSearchField(
                        query = query,
                        onQueryChange = onQueryChange,
                        placeholder = stringResource(Res.string.news_list_search_placeholder),
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
                RssDivider()
            }
        }
        if (articles.isEmpty()) {
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
            items(articles, key = { it.id }) { article ->
                Column {
                    NewsCard(
                        category = article.category,
                        headline = article.headline,
                        byline = article.byline,
                        isFavorite = article.isFavorite,
                        onToggleFavorite = { onToggleFavorite(article) },
                        onClick = { onArticleClick(article) },
                        modifier = Modifier.padding(horizontal = 8.dp),
                        previewUrl = article.previewUrl,
                    )
                    RssDivider()
                }
            }
            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
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
            query = "",
            onQueryChange = {},
            articles = sampleArticles,
            isLoadingMore = false,
            onLoadMore = {},
            onArticleClick = {},
            onToggleFavorite = {},
        )
    }
}
