package com.bunbeauty.fakelivestream.features.stream.view

import androidx.compose.runtime.Immutable
import com.bunbeauty.fakelivestream.common.ui.components.ImageSource

@Immutable
data class ViewState(
    val image: ImageSource<*>,
    val username: String,
    val viewersCount: ViewersCountUi,
    val comments: List<CommentUi>,
    val reactionCount: Int,
    val mode: Mode,
    val isCameraEnabled: Boolean,
    val isCameraFront: Boolean,
    val showJoinRequests: Boolean,
    val showInvite: Boolean,
    val showQuestions: Boolean,
    val showDirect: Boolean,
)

enum class Mode {
    CAMERA,
    VIDEO
}

@Immutable
sealed interface ViewersCountUi {

    @Immutable
    data class UpToThousand(val count: String): ViewersCountUi

    @Immutable
    data class Thousands(
        val thousands: String,
        val hundreds: String,
    ): ViewersCountUi

}

@Immutable
data class CommentUi(
    val picture: ImageSource<*>,
    val username: String,
    val text: String,
)