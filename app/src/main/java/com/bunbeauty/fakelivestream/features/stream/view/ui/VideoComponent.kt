package com.bunbeauty.fakelivestream.features.stream.view.ui

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bunbeauty.fakelivestream.R

@OptIn(UnstableApi::class) @Composable
fun VideoComponent(
    modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current
    val exoPlayer = remember {
        val videoUri = RawResourceDataSource.buildRawResourceUri(R.raw.video)
        ExoPlayer.Builder(localContext).build()
            .apply {
                setMediaItem(MediaItem.fromUri(videoUri))
                prepare()
                playWhenReady = true
            }
    }
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PlayerView(context).apply {
                useController = false
                player = exoPlayer
            }
        }
    )
}