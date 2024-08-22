package com.bunbeauty.tiptoplive.common.navigation

import com.bunbeauty.tiptoplive.common.navigation.NavigationParameters.withBraces

object NavigationDestinations {
    const val INTRO = "intro"
    const val PREPARATION = "preparation"
    const val DONATION = "donation"
    val CROP_IMAGE = "cropImage/${NavigationParameters.URI.withBraces()}"
    const val STREAM = "stream"
}