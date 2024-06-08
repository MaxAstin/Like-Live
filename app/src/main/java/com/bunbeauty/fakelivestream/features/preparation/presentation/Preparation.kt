package com.bunbeauty.fakelivestream.features.preparation.presentation

import android.net.Uri
import com.bunbeauty.fakelivestream.common.presentation.Base
import com.bunbeauty.fakelivestream.common.ui.components.ImageSource
import com.bunbeauty.fakelivestream.features.domain.model.ViewerCount

interface Preparation {

    data class State(
        val image: ImageSource<*>,
        val username: String,
        val viewerCount: ViewerCount,
        val highlightDonate: Boolean,
        val showFeedbackDialog: Boolean,
    ): Base.State

    sealed interface Action: Base.Action {
        data class ViewerCountSelect(val viewerCount: ViewerCount): Action
        data class UsernameUpdate(val username: String): Action
        data object AvatarClick: Action
        data class ImageSelect(val uri: Uri?): Action
        data object StartStreamClick: Action
        data class StreamFinished(val durationInSeconds: Int): Action
        data object CloseFeedbackDialogClick: Action
        data class FeedbackClick(val isPositive: Boolean): Action
        data class NotShowFeedbackChecked(val checked: Boolean): Action
        data object ShareClick: Action
        data object DonateClick: Action
    }

    sealed interface Event: Base.Event {
        data object OpenStream: Event
        data object HandlePositiveFeedbackClick: Event
        data object HandleAvatarClick: Event
        data object HandleShareClick: Event
        data object HandleDonateClick: Event
    }

}