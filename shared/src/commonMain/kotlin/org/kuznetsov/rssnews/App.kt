package org.kuznetsov.rssnews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.koin.compose.viewmodel.koinViewModel
import org.kuznetsov.rssnews.presentation.common.bar.RssBottomNavBar
import org.kuznetsov.rssnews.presentation.common.bar.RssBottomNavDestinations
import org.kuznetsov.rssnews.presentation.common.components.Byline
import org.kuznetsov.rssnews.presentation.feature.article.ArticleScreen
import org.kuznetsov.rssnews.presentation.feature.favourites.FavouritesScreen
import org.kuznetsov.rssnews.presentation.feature.newslist.NewsListScreen
import org.kuznetsov.rssnews.presentation.feature.newslist.NewsListUiState
import org.kuznetsov.rssnews.presentation.feature.newslist.NewsListViewModel
import org.kuznetsov.rssnews.presentation.navigation.AppDestination
import org.kuznetsov.rssnews.presentation.theme.RssNewsTheme

@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .build()
    }
    RssNewsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val viewModel: NewsListViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            val showBottomBar = currentRoute?.startsWith(ArticleRoutePrefix) != true
            val selectedIndex = if (currentRoute == FavouritesRoute) 1 else 0

            Column(modifier = Modifier.safeContentPadding().fillMaxSize()) {
                NavHost(
                    navController = navController,
                    startDestination = AppDestination.NewsList,
                    modifier = Modifier.weight(1f).fillMaxSize(),
                ) {
                    composable<AppDestination.NewsList> {
                        NewsStateGate(state) {
                            NewsListScreen(
                                date = todayLabel(),
                                query = state.query,
                                onQueryChange = viewModel::onQueryChange,
                                articles = state.articles,
                                isLoadingMore = state.isLoadingMore,
                                onLoadMore = viewModel::loadMore,
                                onArticleClick = { navController.navigate(AppDestination.Article(it.id)) },
                                onToggleFavorite = viewModel::onToggleFavourite,
                            )
                        }
                    }
                    composable<AppDestination.Favourites> {
                        val favourites by viewModel.favourites.collectAsStateWithLifecycle()
                        FavouritesScreen(
                            articles = favourites,
                            onArticleClick = { navController.navigate(AppDestination.Article(it.id)) },
                            onToggleFavorite = viewModel::onToggleFavourite,
                        )
                    }
                    composable<AppDestination.Article> { entry ->
                        val route: AppDestination.Article = entry.toRoute()
                        val favourites by viewModel.favourites.collectAsStateWithLifecycle()
                        val article = state.articles.find { it.id == route.articleId }
                            ?: favourites.find { it.id == route.articleId }
                        if (article != null) {
                            ArticleScreen(
                                article = article,
                                onBackClick = { navController.popBackStack() },
                                onToggleFavorite = { viewModel.onToggleFavourite(article) },
                                onShareClick = {},
                                modifier = Modifier.fillMaxSize(),
                            )
                        } else {
                            NewsStateGate(state) {
                                LaunchedEffect(state.articles, favourites) { navController.popBackStack() }
                            }
                        }
                    }
                }

                if (showBottomBar) {
                    RssBottomNavBar(
                        items = RssBottomNavDestinations.items,
                        selectedIndex = selectedIndex,
                        onItemSelected = { index ->
                            val target = if (index == 0) AppDestination.NewsList else AppDestination.Favourites
                            navController.navigate(target) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    }
}

/** Shows [content] once the shared feed has loaded; a spinner/error placeholder otherwise. */
@Composable
private fun NewsStateGate(state: NewsListUiState, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            state.isLoading -> CircularProgressIndicator()
            state.errorMessage != null -> Byline(text = state.errorMessage.orEmpty())
            else -> content()
        }
    }
}

@OptIn(ExperimentalTime::class)
private fun todayLabel(): String = Clock.System.now().toString().substringBefore('T')

private val FavouritesRoute = AppDestination.Favourites::class.qualifiedName
private val ArticleRoutePrefix = AppDestination.Article::class.qualifiedName.orEmpty()
