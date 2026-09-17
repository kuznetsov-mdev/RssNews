package org.kuznetsov.rssnews.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.UriHandler
import platform.Foundation.NSURL
import platform.SafariServices.SFSafariViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

@Composable
actual fun rememberInAppUriHandler(): UriHandler = object : UriHandler {
    override fun openUri(uri: String) {
        val url = NSURL.URLWithString(uri) ?: return
        val safariViewController = SFSafariViewController(uRL = url)
        topViewController()?.presentViewController(safariViewController, animated = true, completion = null)
    }
}

private fun topViewController(): UIViewController? {
    var top = UIApplication.sharedApplication.keyWindow?.rootViewController
    while (top?.presentedViewController != null) {
        top = top.presentedViewController
    }
    return top
}
