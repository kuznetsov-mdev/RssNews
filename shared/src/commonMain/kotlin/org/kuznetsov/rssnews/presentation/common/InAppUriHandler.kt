package org.kuznetsov.rssnews.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.UriHandler

/**
 * A [UriHandler] that opens links in an in-app browser sheet (Chrome Custom Tabs on Android,
 * SFSafariViewController on iOS) instead of leaving the app for a full external browser.
 */
@Composable
expect fun rememberInAppUriHandler(): UriHandler
