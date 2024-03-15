package com.bunbeauty.fakelivestream.features.main.presentation

import com.bunbeauty.fakelivestream.common.presentation.Base

interface Main {

    data class State(val showNoCameraPermission: Boolean): Base.State

    sealed interface Action: Base.Action {
        data object CameraPermissionDeny: Action
        data object CloseCameraRequiredDialogClick: Action
        data object SettingsClick: Action
    }

    sealed interface Event: Base.Event {
        data object OpenSettings: Event
    }

}