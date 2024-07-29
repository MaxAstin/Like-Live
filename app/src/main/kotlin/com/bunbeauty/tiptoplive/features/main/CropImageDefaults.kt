package com.bunbeauty.tiptoplive.features.main

import android.graphics.Color
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

object CropImageDefaults {

    fun options(): CropImageOptions {
        return CropImageOptions(
            imageSourceIncludeCamera = false,
            cropShape = CropImageView.CropShape.OVAL,
            autoZoomEnabled = false,
            fixAspectRatio = true,
            toolbarColor = Color.WHITE,
            activityBackgroundColor = Color.WHITE,
            activityMenuIconColor = Color.BLACK,
            activityMenuTextColor = Color.BLACK,
            toolbarBackButtonColor = Color.BLACK,
        )
    }
}