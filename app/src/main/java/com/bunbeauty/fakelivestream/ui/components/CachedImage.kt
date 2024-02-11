package com.bunbeauty.fakelivestream.ui.components

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

@Immutable
sealed interface ImageSource<T> {

    val data: T

    @Immutable
    data class Device(override val data: Uri) : ImageSource<Uri>

    @Immutable
    data class Url(override val data: String) : ImageSource<String>

    @Immutable
    data class Res(
        @DrawableRes
        override val data: Int,
    ) : ImageSource<Int>
}

@Composable
fun CachedImage(
    imageSource: ImageSource<*>,
    cacheKey: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageSource.data)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.DISABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(cacheKey)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}