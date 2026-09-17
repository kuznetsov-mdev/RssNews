package org.kuznetsov.rssnews.presentation.feature.article

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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Share2
import org.jetbrains.compose.resources.stringResource
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview
import org.kuznetsov.rssnews.presentation.common.components.BackButton
import org.kuznetsov.rssnews.presentation.common.rememberInAppUriHandler
import org.kuznetsov.rssnews.presentation.common.components.Byline
import org.kuznetsov.rssnews.presentation.common.components.FavoriteButton
import org.kuznetsov.rssnews.presentation.common.components.Kicker
import org.kuznetsov.rssnews.presentation.common.components.NewsThumbnail
import org.kuznetsov.rssnews.presentation.common.components.RssIconButton
import org.kuznetsov.rssnews.presentation.common.components.ScreenTitle
import org.kuznetsov.rssnews.presentation.model.ArticleUi
import org.kuznetsov.rssnews.presentation.feature.ScreenPreview
import org.kuznetsov.rssnews.presentation.feature.sampleArticles
import rssnews.shared.generated.resources.Res
import rssnews.shared.generated.resources.article_read_more_link
import rssnews.shared.generated.resources.article_read_more_prefix
import rssnews.shared.generated.resources.article_read_more_suffix

/** The article reader: lead photo, headline and body copy under a back/favourite/share header. */
@Composable
fun ArticleScreen(
    article: ArticleUi,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalUriHandler provides rememberInAppUriHandler()) {
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

            NewsThumbnail(
                previewUrl = article.previewUrl,
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
                text = buildAnnotatedString {
                    append(article.body)
                    append(stringResource(Res.string.article_read_more_prefix))
                    withLink(
                        LinkAnnotation.Url(
                            url = article.sourceUrl,
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline,
                                ),
                            ),
                        )
                    ) {
                        append(stringResource(Res.string.article_read_more_link))
                    }
                    append(stringResource(Res.string.article_read_more_suffix))
                },
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
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
