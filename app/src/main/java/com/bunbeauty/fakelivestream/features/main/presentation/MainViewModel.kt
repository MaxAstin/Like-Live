package com.bunbeauty.fakelivestream.features.main.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.features.domain.SaveImageUriUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveImageUriUseCase: SaveImageUriUseCase
) : BaseViewModel<Main.State, Main.Action, Main.Event>(
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

            is Main.Action.AvatarSelected -> {
                viewModelScope.launch {
                    action.uri?.let { uri ->
                        saveImageUriUseCase(uri.toString())
                    }
                }
            }
        }
    }

}