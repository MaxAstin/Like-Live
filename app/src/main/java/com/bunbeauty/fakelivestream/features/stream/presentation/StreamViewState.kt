package com.bunbeauty.fakelivestream.features.stream.presentation

import androidx.compose.runtime.Immutable
import com.bunbeauty.fakelivestream.ui.components.ImageSource

@Immutable
data class StreamViewState(
    val image: ImageSource<*>,
    val username: String,
    val viewersCount: ViewersCount,
    val comments: List<CommentUi>,
)

@Immutable
sealed interface ViewersCount {

    @Immutable
    data class UpToThousand(val count: String): ViewersCount

    @Immutable
    data class Thousands(
        val thousands: String,
        val hundreds: String,
    ): ViewersCount

}

@Immutable
data class CommentUi(
    val picture: ImageSource<*>,
    val username: String,
    val text: String,
)