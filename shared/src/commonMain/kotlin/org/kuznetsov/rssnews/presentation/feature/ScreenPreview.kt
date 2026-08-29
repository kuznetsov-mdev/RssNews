package org.kuznetsov.rssnews.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.kuznetsov.rssnews.presentation.theme.RssNewsTheme

/** Wraps a screen `@Preview` in [RssNewsTheme] over a full-bleed themed background. */
@Composable
internal fun ScreenPreview(content: @Composable () -> Unit) {
    RssNewsTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}
