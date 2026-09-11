package org.kuznetsov.rssnews

import androidx.compose.ui.window.ComposeUIViewController
import org.kuznetsov.rssnews.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}