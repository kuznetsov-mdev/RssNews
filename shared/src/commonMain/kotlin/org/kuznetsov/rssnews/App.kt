package org.kuznetsov.rssnews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.kuznetsov.rssnews.presentation.common.bar.RssBottomNavBar
import org.kuznetsov.rssnews.presentation.common.bar.RssBottomNavDestinations
import org.kuznetsov.rssnews.presentation.common.components.ScreenTitle
import org.kuznetsov.rssnews.presentation.theme.RssNewsTheme

@Composable
@Preview
fun App() {
    RssNewsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var selectedIndex by remember { mutableStateOf(0) }
            Column(modifier = Modifier.safeContentPadding().fillMaxSize()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    ScreenTitle(text = RssBottomNavDestinations.items[selectedIndex].label)
                }
                RssBottomNavBar(
                    items = RssBottomNavDestinations.items,
                    selectedIndex = selectedIndex,
                    onItemSelected = { selectedIndex = it },
                )
            }
        }
    }
}
