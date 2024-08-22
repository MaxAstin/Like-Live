package com.bunbeauty.tiptoplive.features.cropimage

import android.net.Uri
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bunbeauty.tiptoplive.common.navigation.NavigationParameters.CROPPED_IMAGE_URI
import com.bunbeauty.tiptoplive.features.main.CropImageDefaults
import com.canhub.cropper.CropImageView

@Composable
fun CropImageScreen(
    navController: NavController,
    uri: Uri?
) {
    CropImageView(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        uri = uri,
    )
}


@Composable
private fun CropImageView(
    navController: NavController,
    uri: Uri?,
    modifier: Modifier = Modifier
) {
    uri ?: return

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.align(Alignment.Center),
            factory = { context ->
                LinearLayout(context).apply {
                    val cropImageView = CropImageView(context).apply {
                        setImageCropOptions(CropImageDefaults.options())
                        setImageUriAsync(uri)
                    }.also { view ->
                        val listener = CropImageView.OnCropImageCompleteListener { _, result ->
                            navController.popBackStack()
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set(CROPPED_IMAGE_URI, result.uriContent)
                        }
                        view.setOnCropImageCompleteListener(listener)
                    }
                    addView(cropImageView)
                }
            }
        )
    }
}