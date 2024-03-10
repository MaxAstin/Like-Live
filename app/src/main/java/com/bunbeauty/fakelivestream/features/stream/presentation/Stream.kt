package com.bunbeauty.fakelivestream.features.stream.presentation

import android.net.Uri
import com.bunbeauty.fakelivestream.common.presentation.Base
import com.bunbeauty.fakelivestream.features.stream.domain.Comment

interface Stream {

    data class State(
        val imageUri: Uri?,
        val username: String,
        val viewersCount: Int,
        val comments: List<Comment>,
        val reactionCount: Int,
        val showJoinRequests: Boolean,
        val showInvite: Boolean,
        val showQuestions: Boolean,
        val showDirect: Boolean,
    ): Base.State

    sealed interface Action: Base.Action {
        data object ShowJoinRequests: Action
        data object HideJoinRequests: Action
        data object ShowInvite: Action
        data object HideInvite: Action
        data object ShowQuestions: Action
        data object HideQuestions: Action
        data object ShowDirect: Action
        data object HideDirect: Action
        data object Start: Action
        data object Stop: Action
        data object FinishStreamClick: Action
    }

    sealed interface Event: Base.Event {
        data object GoBack: Event
    }
}