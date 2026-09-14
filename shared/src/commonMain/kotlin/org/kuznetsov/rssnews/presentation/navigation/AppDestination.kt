package org.kuznetsov.rssnews.presentation.navigation

import kotlinx.serialization.Serializable

/** Every screen reachable in the app, as type-safe navigation-compose routes. */
@Serializable
sealed interface AppDestination {
    @Serializable
    data object NewsList : AppDestination

    @Serializable
    data object Favourites : AppDestination

    @Serializable
    data class Article(val articleId: String) : AppDestination
}
