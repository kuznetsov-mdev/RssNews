package org.kuznetsov.rssnews.presentation.common

import androidx.compose.ui.tooling.preview.Preview

/** Renders an annotated `@Preview` composable twice: once in light, once in dark theme. */
@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = 0x20)
annotation class LightDarkPreview
