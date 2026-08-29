package org.kuznetsov.rssnews.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.kuznetsov.rssnews.presentation.theme.color.DarkColorScheme
import org.kuznetsov.rssnews.presentation.theme.color.LightColorScheme
import org.kuznetsov.rssnews.presentation.theme.type.rssNewsTypography

@Composable
fun RssNewsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = rssNewsTypography(),
        content = content,
    )
}
