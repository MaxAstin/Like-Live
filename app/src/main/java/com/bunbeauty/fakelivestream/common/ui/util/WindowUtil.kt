package com.bunbeauty.fakelivestream.common.ui.util

import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON

var Window.keepScreenOn: Boolean
    get() {
        return attributes.flags.and(FLAG_KEEP_SCREEN_ON) != 0
    }
    set(value) {
        if (value) {
            addFlags(FLAG_KEEP_SCREEN_ON)
        } else {
            clearFlags(FLAG_KEEP_SCREEN_ON)
        }
    }