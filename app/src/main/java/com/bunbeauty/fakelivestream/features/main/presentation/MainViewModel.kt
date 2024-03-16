package com.bunbeauty.fakelivestream.features.main.presentation

import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(): BaseViewModel<Main.State, Main.Action, Main.Event>(
    initState = {
        Main.State(showNoCameraPermission = false)
    }
) {

    override fun onAction(action: Main.Action) {
        when (action) {
            Main.Action.CameraPermissionDeny -> {
                setState {
                    copy(showNoCameraPermission = true)
                }
            }
            Main.Action.CameraPermissionAccept -> {
                sendEvent(Main.Event.OpenStream)
            }
            Main.Action.CloseCameraRequiredDialogClick -> {
                setState {
                    copy(showNoCameraPermission = false)
                }
            }
            Main.Action.SettingsClick -> {
                sendEvent(Main.Event.OpenSettings)
            }
        }
    }

}