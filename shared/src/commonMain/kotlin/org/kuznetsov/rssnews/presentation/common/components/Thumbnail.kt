package org.kuznetsov.rssnews.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.kuznetsov.rssnews.presentation.common.ComponentPreview
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview

/**
 * The diagonally-hatched "no photo yet" placeholder used wherever a story
 * thumbnail or lead photo would go, labelled with a [Kicker].
 */
@Composable
fun PlaceholderThumbnail(
    modifier: Modifier = Modifier,
    label: String = "Photo",
    cornerRadius: Dp = 4.dp,
) {
    val hatchColor = MaterialTheme.colorScheme.outlineVariant
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .diagonalHatch(hatchColor),
        contentAlignment = Alignment.Center,
    ) {
        Kicker(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun Modifier.diagonalHatch(
    lineColor: Color,
    spacing: Dp = 10.dp,
): Modifier = drawBehind {
    val spacingPx = spacing.toPx()
    val strokeWidthPx = 1.dp.toPx()
    val diagonal = size.width + size.height
    var offset = -size.height
    while (offset < diagonal) {
        drawLine(
            color = lineColor,
            start = Offset(offset, size.height),
            end = Offset(offset + size.height, 0f),
            strokeWidth = strokeWidthPx,
        )
        offset += spacingPx
    }
}

@LightDarkPreview
@Composable
private fun PlaceholderThumbnailPreview() {
    ComponentPreview {
        PlaceholderThumbnail(modifier = Modifier.size(96.dp))
    }
}
