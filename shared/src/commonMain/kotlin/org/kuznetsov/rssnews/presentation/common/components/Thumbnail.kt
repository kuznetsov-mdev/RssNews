package org.kuznetsov.rssnews.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import org.kuznetsov.rssnews.presentation.common.ComponentPreview
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview

/**
 * A story thumbnail loaded from [previewUrl]. Falls back to [PlaceholderThumbnail]
 * when there's no url, the image is still loading, or it failed to load.
 */
@Composable
fun NewsThumbnail(
    previewUrl: String?,
    modifier: Modifier = Modifier,
    label: String = "News",
    cornerRadius: Dp = 4.dp,
) {
    if (previewUrl.isNullOrBlank()) {
        PlaceholderThumbnail(modifier = modifier, label = label, cornerRadius = cornerRadius)
    } else {
        SubcomposeAsyncImage(
            model = previewUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(RoundedCornerShape(cornerRadius)),
            loading = {
                PlaceholderThumbnail(modifier = Modifier.fillMaxSize(), label = label, cornerRadius = cornerRadius)
            },
            error = {
                PlaceholderThumbnail(modifier = Modifier.fillMaxSize(), label = label, cornerRadius = cornerRadius)
            },
        )
    }
}

/**
 * The diagonally-striped "no photo yet" placeholder used wherever a story
 * thumbnail or lead photo would go, labelled with a [Kicker].
 */
@Composable
fun PlaceholderThumbnail(
    modifier: Modifier = Modifier,
    label: String = "News",
    cornerRadius: Dp = 4.dp,
) {
    val baseColor = MaterialTheme.colorScheme.surfaceVariant
    val stripeColor = MaterialTheme.colorScheme.outline
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .diagonalStripes(baseColor, stripeColor),
        contentAlignment = Alignment.Center,
    ) {
        Kicker(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun Modifier.diagonalStripes(
    baseColor: Color,
    stripeColor: Color,
    bandWidth: Dp = 6.dp,
): Modifier = drawBehind {
    drawRect(baseColor)
    val bandPx = bandWidth.toPx()
    val period = bandPx * 2
    val diagonal = size.width + size.height
    var offset = -size.height
    while (offset < diagonal) {
        drawLine(
            color = stripeColor,
            start = Offset(offset, size.height),
            end = Offset(offset + size.height, 0f),
            strokeWidth = bandPx,
        )
        offset += period
    }
}

@LightDarkPreview
@Composable
private fun PlaceholderThumbnailPreview() {
    ComponentPreview {
        PlaceholderThumbnail(modifier = Modifier.size(96.dp))
    }
}
