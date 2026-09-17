package org.kuznetsov.rssnews.presentation.common

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.UriHandler
import androidx.core.net.toUri

@Composable
actual fun rememberInAppUriHandler(): UriHandler {
    val context = LocalContext.current
    return remember(context) {
        object : UriHandler {
            override fun openUri(uri: String) {
                CustomTabsIntent.Builder()
                    .build()
                    .launchUrl(context, uri.toUri())
            }
        }
    }
}
