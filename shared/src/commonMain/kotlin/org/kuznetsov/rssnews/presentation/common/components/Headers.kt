package org.kuznetsov.rssnews.presentation.common.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

/**
 * A small uppercase, letter-spaced mono label — used for section eyebrows
 * ("BROADSHEET · MOBILE"), story categories ("CLIMATE") and placeholders ("PHOTO").
 */
@Composable
fun Kicker(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        color = color,
        style = MaterialTheme.typography.labelSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/** A large serif heading — used for screen titles ("Today", "Favourites") and card/article headlines. */
@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onBackground,
        style = style,
    )
}

/** Small muted metadata line — used for the byline ("Nadia Ferreira · 6 min read"). */
@Composable
fun Byline(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
