package com.bunbeauty.fakelivestream.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Gray300 = Color(0xFF6D6D6D)
val Gray200 = Color(0xFF888888)
val Gray100 = Color(0xFFDBDBDB)
val Blue = Color(0xFF0195F7)
val Pink = Color(0xFFDB0D67)
val BrightPurple = Color(0xFFCD00BD)
val Scarlet = Color(0xFFFF1E44)
val Amber = Color(0xFFFFBF00)

@Stable
class ColorScheme(
    interactive: Color,
    icon: Color,
    iconVariant: Color,
    surface: Color,
    onSurface: Color,
    onSurfaceVariant: Color,
    background: Color,
    onBackground: Color,
    border: Color,
    borderVariant: Color,
    instagram: InstagramColors,
) {

    var interactive by mutableStateOf(interactive)
        internal set

    var icon by mutableStateOf(icon)
        internal set

    var iconVariant by mutableStateOf(iconVariant)
        internal set

    var surface by mutableStateOf(surface)
        internal set

    var onSurface by mutableStateOf(onSurface)
        internal set

    var onSurfaceVariant by mutableStateOf(onSurfaceVariant)
        internal set

    var background by mutableStateOf(background)
        internal set

    var onBackground by mutableStateOf(onBackground)
        internal set

    var border by mutableStateOf(border)
        internal set

    var borderVariant by mutableStateOf(borderVariant)
        internal set

    var instagram by mutableStateOf(instagram)
        internal set


    fun copy(
        interactive: Color = this.interactive,
        icon: Color = this.icon,
        iconVariant: Color = this.iconVariant,
        surface: Color = this.surface,
        onSurface: Color = this.onSurface,
        onSurfaceVariant: Color = this.onSurfaceVariant,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        border: Color = this.border,
        borderVariant: Color = this.borderVariant,
        instagram: InstagramColors = this.instagram,
    ): ColorScheme = ColorScheme(
        interactive = interactive,
        icon = icon,
        iconVariant = iconVariant,
        surface = surface,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        background = background,
        onBackground = onBackground,
        border = border,
        borderVariant = borderVariant,
        instagram = instagram.copy(
            logo1 = instagram.logo1,
            logo2 = instagram.logo2,
            logo3 = instagram.logo3,
            accent = instagram.accent,
        ),
    )
}

@Stable
class InstagramColors(
    logo1: Color,
    logo2: Color,
    logo3: Color,
    accent: Color,
) {
    var logo1 by mutableStateOf(logo1)
        internal set

    var logo2 by mutableStateOf(logo2)
        internal set

    var logo3 by mutableStateOf(logo3)
        internal set

    var accent by mutableStateOf(accent)
        internal set

    fun copy(
        logo1: Color = this.logo1,
        logo2: Color = this.logo2,
        logo3: Color = this.logo3,
        accent: Color = this.accent,
    ): InstagramColors = InstagramColors(
        logo1 = logo1,
        logo2 = logo2,
        logo3 = logo3,
        accent = accent,
    )

}