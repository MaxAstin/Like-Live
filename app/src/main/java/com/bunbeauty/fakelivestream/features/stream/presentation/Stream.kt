package com.bunbeauty.fakelivestream.features.stream.presentation

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.bunbeauty.fakelivestream.features.stream.domain.Comment
import com.bunbeauty.fakelivestream.ui.components.ImageSource

interface Stream {

    data class DataState(
        val imageUri: Uri?,
        val username: String,
        val viewersCount: Int,
        val comments: List<Comment>,
        val reactionCount: Int,
        val showJoinRequests: Boolean,
        val showInvite: Boolean,
        val showQuestions: Boolean,
        val showDirect: Boolean,
    )

    @Immutable
    data class ViewState(
        val image: ImageSource<*>,
        val username: String,
        val viewersCount: ViewersCountUi,
        val comments: List<CommentUi>,
        val reactionCount: Int,
        val showCamera: Boolean,
        val showJoinRequests: Boolean,
        val showInvite: Boolean,
        val showQuestions: Boolean,
        val showDirect: Boolean,
    )

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

    sealed interface Action {
        data object ShowJoinRequests: Action
        data object HideJoinRequests: Action
        data object ShowInvite: Action
        data object HideInvite: Action
        data object ShowQuestions: Action
        data object HideQuestions: Action
        data object ShowDirect: Action
        data object HideDirect: Action
    }
}