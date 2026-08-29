package org.kuznetsov.rssnews.presentation.common.bar

import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Newspaper

/** The app's default bottom nav destinations: the news list and the favourites tab. */
object RssBottomNavDestinations {
    val News = BottomNavItem(label = "News", icon = Lucide.Newspaper)
    val Favourites = BottomNavItem(label = "Favourites", icon = Lucide.Heart)

    val items = listOf(News, Favourites)
}
