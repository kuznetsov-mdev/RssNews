package org.kuznetsov.rssnews.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Share2
import org.kuznetsov.rssnews.presentation.common.ComponentPreview
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview

/** The pill-shaped accent button used across the app (e.g. primary calls to action). */
@Composable
fun RssPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

/** A generic icon-only button, used for chrome actions like share. */
@Composable
fun RssIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = icon, contentDescription = contentDescription, tint = tint)
    }
}

/** The heart toggle used on news cards, the article header and the favourites list. */
@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onToggle, modifier = modifier) {
        Icon(
            imageVector = Lucide.Heart,
            contentDescription = if (isFavorite) "Remove from favourites" else "Add to favourites",
            tint = if (isFavorite) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}

/** The "← Back" navigation control used at the top of the article screen. */
@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Back",
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Lucide.ArrowLeft,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 6.dp),
        )
    }
}

@LightDarkPreview
@Composable
private fun RssPrimaryButtonPreview() {
    ComponentPreview {
        RssPrimaryButton(text = "Click me!", onClick = {})
    }
}

@LightDarkPreview
@Composable
private fun RssIconButtonPreview() {
    ComponentPreview {
        RssIconButton(icon = Lucide.Share2, contentDescription = "Share", onClick = {})
    }
}

@LightDarkPreview
@Composable
private fun FavoriteButtonPreview() {
    ComponentPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FavoriteButton(isFavorite = false, onToggle = {})
            FavoriteButton(isFavorite = true, onToggle = {})
        }
    }
}

@LightDarkPreview
@Composable
private fun BackButtonPreview() {
    ComponentPreview {
        BackButton(onClick = {})
    }
}
