package com.bunbeauty.tiptoplive.features.stream.view.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.bunbeauty.tiptoplive.common.ui.components.CachedImage
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource

@Composable
fun AvatarImage(
    image: ImageSource<*>,
    modifier: Modifier = Modifier,
) {
    CachedImage(
        modifier = modifier.clip(CircleShape),
        imageSource = image,
        cacheKey = image.data.toString(),
        contentDescription = "Avatar"
    )
}