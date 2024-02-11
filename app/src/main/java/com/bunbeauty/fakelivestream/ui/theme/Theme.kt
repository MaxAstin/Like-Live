package com.bunbeauty.fakelivestream.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

private val DarkColorScheme = ColorScheme(
    interactive = Blue,
    icon = White,
    iconVariant = Gray200,
    surface = Black,
    onSurface = White,
    onSurfaceVariant = Gray200,
    background = White,
    onBackground = Black,
    border = Gray300,
    borderVariant = Gray100,
    instagram = InstagramColors(
        logo1 = BrightPurple,
        logo2 = Scarlet,
        logo3 = Amber,
        accent = Pink,
    ),
)

private val LightColorScheme = ColorScheme(
    interactive = Blue,
    icon = White,
    iconVariant = Gray200,
    surface = Black,
    onSurface = White,
    onSurfaceVariant = Gray200,
    background = White,
    onBackground = Black,
    border = Gray300,
    borderVariant = Gray100,
    instagram = InstagramColors(
        logo1 = BrightPurple,
        logo2 = Scarlet,
        logo3 = Amber,
        accent = Pink,
    ),
)

val LocalFakeLiveStreamColors = staticCompositionLocalOf { LightColorScheme }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FakeLiveStreamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val rememberedColors = remember {
        colorScheme.copy()
    }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null,
        LocalFakeLiveStreamColors provides rememberedColors,
        LocalFakeLiveStreamTypography provides FakeLiveStreamTypography(),
        content = content
    )
}

object FakeLiveStreamTheme {
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalFakeLiveStreamColors.current
    val typography: FakeLiveStreamTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalFakeLiveStreamTypography.current
}