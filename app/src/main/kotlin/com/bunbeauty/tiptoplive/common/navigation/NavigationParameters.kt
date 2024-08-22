package com.bunbeauty.tiptoplive.common.navigation

object NavigationParameters {
    const val URI = "uri"
    const val CROPPED_IMAGE_URI = "uri"

    fun String.withBraces(): String {
        return "{$this}"
    }
}