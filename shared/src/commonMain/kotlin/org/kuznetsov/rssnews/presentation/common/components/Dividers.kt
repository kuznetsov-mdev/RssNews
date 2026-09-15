package org.kuznetsov.rssnews.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kuznetsov.rssnews.presentation.common.ComponentPreview
import org.kuznetsov.rssnews.presentation.common.LightDarkPreview

/** A hairline rule, used between list rows and under section headers. */
@Composable
fun RssDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outline,
    )
}

@LightDarkPreview
@Composable
private fun RssDividerPreview() {
    ComponentPreview {
        RssDivider(modifier = Modifier.fillMaxWidth())
    }
}
