package org.kuznetsov.rssnews.presentation.theme.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import rssnews.shared.generated.resources.Res
import rssnews.shared.generated.resources.pt_mono_regular
import rssnews.shared.generated.resources.pt_sans_bold
import rssnews.shared.generated.resources.pt_sans_regular
import rssnews.shared.generated.resources.pt_serif_bold
import rssnews.shared.generated.resources.pt_serif_bold_italic
import rssnews.shared.generated.resources.pt_serif_italic
import rssnews.shared.generated.resources.pt_serif_regular

/**
 * The "Broadsheet" type system: a serif for headlines and reading copy,
 * a sans for UI chrome and metadata, and a mono for kickers/labels.
 * https://claude.ai/design/p/ae885857-4c3b-4b3c-9a8a-b91788983112
 */

@Composable
fun ptSerifFamily(): FontFamily = FontFamily(
    Font(Res.font.pt_serif_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(Res.font.pt_serif_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(Res.font.pt_serif_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(Res.font.pt_serif_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
)

@Composable
fun ptSansFamily(): FontFamily = FontFamily(
    Font(Res.font.pt_sans_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(Res.font.pt_sans_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
)

@Composable
fun ptMonoFamily(): FontFamily = FontFamily(
    Font(Res.font.pt_mono_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
)
