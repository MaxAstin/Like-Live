package com.bunbeauty.fakelivestream.ui.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.RippleTheme.Companion.defaultRippleAlpha
import androidx.compose.material.ripple.RippleTheme.Companion.defaultRippleColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class FakeLiveRippleTheme(private val lightTheme: Boolean) : RippleTheme {

    @Composable
    override fun defaultColor(): Color {
        return defaultRippleColor(
            contentColor = FakeLiveStreamTheme.colors.background,
            lightTheme = lightTheme
        )
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return defaultRippleAlpha(
            contentColor = FakeLiveStreamTheme.colors.background,
            lightTheme = lightTheme
        )
    }
}