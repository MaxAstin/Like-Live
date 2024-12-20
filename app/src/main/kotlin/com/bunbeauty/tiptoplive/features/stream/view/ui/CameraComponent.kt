package com.bunbeauty.tiptoplive.features.stream.view.ui

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.tiptoplive.common.util.getCameraProvider

@Composable
fun CameraComponent(
    isFront: Boolean,
    isEnabled: Boolean,
    image: ImageSource<*>,
    onCameraError: (Exception) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isEnabled) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val previewView = remember { PreviewView(context) }

        LaunchedEffect(isFront) {
            val cameraProvider = context.getCameraProvider()
            val lensFacing = if (isFront) {
                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                    CameraSelector.LENS_FACING_FRONT
                } else {
                    return@LaunchedEffect
                }
            } else {
                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                    CameraSelector.LENS_FACING_BACK
                } else {
                    return@LaunchedEffect
                }
            }
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            val preview = Preview.Builder().build()
            val imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exception: Exception) {
                onCameraError(exception)
            }

            preview.setSurfaceProvider(previewView.surfaceProvider)
        }

        AndroidView(
            factory = { previewView },
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier
                .background(FakeLiveStreamTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            AvatarImage(
                image = image,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}