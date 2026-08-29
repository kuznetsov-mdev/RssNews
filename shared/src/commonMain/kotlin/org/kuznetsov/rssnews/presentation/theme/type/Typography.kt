package org.kuznetsov.rssnews.presentation.theme.type

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun rssNewsTypography(): Typography {
    val serif = ptSerifFamily()
    val sans = ptSansFamily()
    val mono = ptMonoFamily()

    return Typography(
        displayLarge = TextStyle(fontFamily = serif, fontWeight = FontWeight.Bold, fontSize = 45.sp, lineHeight = 52.sp),
        displayMedium = TextStyle(fontFamily = serif, fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 44.sp),
        displaySmall = TextStyle(fontFamily = serif, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 40.sp),

        headlineLarge = TextStyle(fontFamily = serif, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp),
        headlineMedium = TextStyle(fontFamily = serif, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp),
        headlineSmall = TextStyle(fontFamily = serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 26.sp),

        titleLarge = TextStyle(fontFamily = serif, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 24.sp),
        titleMedium = TextStyle(fontFamily = sans, fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 22.sp),
        titleSmall = TextStyle(fontFamily = sans, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp),

        bodyLarge = TextStyle(fontFamily = serif, fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 26.sp),
        bodyMedium = TextStyle(fontFamily = serif, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp),
        bodySmall = TextStyle(fontFamily = serif, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),

        labelLarge = TextStyle(fontFamily = sans, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp),
        labelMedium = TextStyle(fontFamily = sans, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),
        labelSmall = TextStyle(
            fontFamily = mono,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.08.em,
        ),
    )
}
