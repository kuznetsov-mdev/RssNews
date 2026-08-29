package org.kuznetsov.rssnews.presentation.theme.color

import androidx.compose.ui.graphics.Color

/**
 * Palette from the "Broadsheet" design (light = paper, dark = ink).
 * https://claude.ai/design/p/ae885857-4c3b-4b3c-9a8a-b91788983112
 */

// Paper (light) surfaces and text
val PaperBackground = Color(0xFFF3F2F2)
val PaperSurface = Color(0xFFEAE9E9)
val InkText = Color(0xFF201E1D)

// Ink (dark) surfaces and text — the paper/ink roles swap
val InkBackground = Color(0xFF201E1D)
val InkSurface = Color(0xFF2D2B2B)
val InkSurfaceVariant = Color(0xFF444141)
val PaperText = Color(0xFFF3F2F2)

// Neutral ramp
val Neutral100 = Color(0xFFF8F4F4)
val Neutral200 = Color(0xFFEAE7E7)
val Neutral300 = Color(0xFFD7D3D3)
val Neutral400 = Color(0xFFBAB6B6)
val Neutral500 = Color(0xFF9B9797)
val Neutral600 = Color(0xFF7D7979)
val Neutral700 = Color(0xFF605D5D)
val Neutral800 = Color(0xFF444141)
val Neutral900 = Color(0xFF2D2B2B)

// Accent ramp — cyan (primary)
val Accent100 = Color(0xFFE9F8FF)
val Accent200 = Color(0xFFCBEEFF)
val Accent300 = Color(0xFF99E0FF)
val Accent400 = Color(0xFF62C5EE)
val Accent500 = Color(0xFF38A6CF)
val Accent600 = Color(0xFF1186AC)
val Accent700 = Color(0xFF006786)
val Accent800 = Color(0xFF004961)
val Accent900 = Color(0xFF0A303E)
val AccentBase = Color(0xFF0088B0)

// Accent-2 ramp — magenta (secondary, used for the favourite heart)
val Accent2_100 = Color(0xFFFFF1F4)
val Accent2_200 = Color(0xFFFFDEE6)
val Accent2_300 = Color(0xFFFFC0D0)
val Accent2_400 = Color(0xFFFF90B1)
val Accent2_500 = Color(0xFFFF458E)
val Accent2_600 = Color(0xFFD82071)
val Accent2_700 = Color(0xFFAA0B56)
val Accent2_800 = Color(0xFF790E3D)
val Accent2_900 = Color(0xFF4B1528)
val Accent2Base = Color(0xFFD6006C)

// Process yellow — print-treatment color, not used for interface chrome
val ProcessYellow = Color(0xFFEDBB00)
