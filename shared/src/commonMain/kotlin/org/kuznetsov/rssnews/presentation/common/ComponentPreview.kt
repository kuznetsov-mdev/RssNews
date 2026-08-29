package org.kuznetsov.rssnews.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kuznetsov.rssnews.presentation.theme.RssNewsTheme

/**
 * Wraps a `@Preview` composable in [RssNewsTheme] over an opaque background,
 * so the light/dark preview pair renders with real theme colours.
 */
@Composable
internal fun ComponentPreview(content: @Composable () -> Unit) {
    RssNewsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}
