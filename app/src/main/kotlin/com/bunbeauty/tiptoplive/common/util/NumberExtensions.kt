package com.bunbeauty.tiptoplive.common.util

fun Int.roundToEvent(): Int {
    return if (this % 2 == 0) {
        this
    } else {
        this - 1
    }
}