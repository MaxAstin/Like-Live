package com.bunbeauty.fakelivestream.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bunbeauty.fakelivestream.R

internal val LocalFakeLiveStreamTypography = staticCompositionLocalOf { FakeLiveStreamTypography() }

@Immutable
data class FakeLiveStreamTypography(
    val title: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    val bodySmall: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 18.sp
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
//    val action: TextStyle = TextStyle(
//        fontFamily = FontFamily(Font(R.font.roboto_medium)),
//        fontWeight = FontWeight.W500,
//        fontSize = 14.sp,
//        lineHeight = 20.sp
//    ),
)