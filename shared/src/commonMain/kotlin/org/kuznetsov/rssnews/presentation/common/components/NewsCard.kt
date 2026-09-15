package org.kuznetsov.rssnews.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kuznetsov.rssnews.presentation.common.ComponentPreview
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview

/**
 * A story row: thumbnail, category kicker, headline and byline, with a
 * favourite toggle — the repeating unit of the news list and favourites tab.
 */
@Composable
fun NewsCard(
    category: String,
    headline: String,
    byline: String,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    previewUrl: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        NewsThumbnail(previewUrl = previewUrl, modifier = Modifier.size(96.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            Kicker(text = category)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = headline,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 16.sp, lineHeight = 24.sp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Byline(text = byline)
        }

        FavoriteButton(
            isFavorite = isFavorite,
            onToggle = onToggleFavorite,
            modifier = Modifier.width(40.dp),
        )
    }
}

@LightDarkPreview
@Composable
private fun NewsCardPreview() {
    ComponentPreview {
        NewsCard(
            category = "Climate",
            headline = "Rivers reroute as the delta drains a second summer",
            byline = "Nadia Ferreira · 6 min read",
            isFavorite = false,
            onToggleFavorite = {},
            onClick = {},
        )
    }
}
