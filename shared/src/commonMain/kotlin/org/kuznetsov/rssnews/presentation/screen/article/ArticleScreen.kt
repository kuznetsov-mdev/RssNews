package org.kuznetsov.rssnews.presentation.screen.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Share2
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview
import org.kuznetsov.rssnews.presentation.common.components.BackButton
import org.kuznetsov.rssnews.presentation.common.components.Byline
import org.kuznetsov.rssnews.presentation.common.components.FavoriteButton
import org.kuznetsov.rssnews.presentation.common.components.Kicker
import org.kuznetsov.rssnews.presentation.common.components.PlaceholderThumbnail
import org.kuznetsov.rssnews.presentation.common.components.RssIconButton
import org.kuznetsov.rssnews.presentation.common.components.ScreenTitle
import org.kuznetsov.rssnews.presentation.model.ArticleUi
import org.kuznetsov.rssnews.presentation.screen.ScreenPreview
import org.kuznetsov.rssnews.presentation.screen.sampleArticles

/** The article reader: lead photo, headline and body copy under a back/favourite/share header. */
@Composable
fun ArticleScreen(
    article: ArticleUi,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BackButton(onClick = onBackClick)
            Spacer(modifier = Modifier.weight(1f))
            FavoriteButton(isFavorite = article.isFavorite, onToggle = onToggleFavorite)
            RssIconButton(icon = Lucide.Share2, contentDescription = "Share", onClick = onShareClick)
        }

        PlaceholderThumbnail(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            label = "Lead photo",
            cornerRadius = 8.dp,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Kicker(text = article.category)
        Spacer(modifier = Modifier.height(8.dp))
        ScreenTitle(text = article.headline, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Byline(text = article.byline)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = article.body,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@LightDarkPreview
@Composable
private fun ArticleScreenPreview() {
    ScreenPreview {
        ArticleScreen(
            article = sampleArticles.first(),
            onBackClick = {},
            onToggleFavorite = {},
            onShareClick = {},
        )
    }
}
