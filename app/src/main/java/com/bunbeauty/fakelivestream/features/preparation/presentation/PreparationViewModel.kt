package com.bunbeauty.fakelivestream.features.preparation.presentation

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.analytics.AnalyticsManager
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.common.ui.components.ImageSource
import com.bunbeauty.fakelivestream.features.domain.GetImageUriUseCase
import com.bunbeauty.fakelivestream.features.domain.GetUsernameUseCase
import com.bunbeauty.fakelivestream.features.domain.GetViewerCountUseCase
import com.bunbeauty.fakelivestream.features.domain.SaveImageUriUseCase
import com.bunbeauty.fakelivestream.features.domain.SaveUsernameUseCase
import com.bunbeauty.fakelivestream.features.domain.SaveViewerCountUseCase
import com.bunbeauty.fakelivestream.features.domain.model.ViewerCount
import com.bunbeauty.fakelivestream.features.preparation.domain.SaveFeedbackAskedUseCase
import com.bunbeauty.fakelivestream.features.preparation.domain.ShouldAskFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparationViewModel @Inject constructor(
    private val getImageUriUseCase: GetImageUriUseCase,
    private val saveImageUriUseCase: SaveImageUriUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val saveUsernameUseCase: SaveUsernameUseCase,
    private val getViewerCountUseCase: GetViewerCountUseCase,
    private val saveViewerCountUseCase: SaveViewerCountUseCase,
    private val shouldAskFeedbackUseCase: ShouldAskFeedbackUseCase,
    private val saveFeedbackAskedUseCase: SaveFeedbackAskedUseCase,
    private val analyticsManager: AnalyticsManager,
) : BaseViewModel<Preparation.State, Preparation.Action, Preparation.Event>(
    initState = {
        Preparation.State(
            image = ImageSource.ResId(R.drawable.img_default_avatar),
            username = "",
            viewerCount = ViewerCount.V_100_200,
            showFeedbackDialog = false,
        )
    }
) {

    init {
        initState()
        observeAndSaveUsername()
    }

    override fun onAction(action: Preparation.Action) {
        when (action) {
            is Preparation.Action.ImageSelect -> {
                action.uri?.let { imageUri ->
                    viewModelScope.launch {
                        saveImageUriUseCase(uri = imageUri.toString())
                    }
                    mutableState.update { state ->
                        state.copy(image = ImageSource.Device(imageUri))
                    }
                }
            }

            is Preparation.Action.UsernameUpdate -> {
                mutableState.update { state ->
                    state.copy(username = action.username)
                }
            }

            is Preparation.Action.ViewerCountSelect -> {
                viewModelScope.launch {
                    saveViewerCountUseCase(viewerCount = action.viewerCount)
                }
                mutableState.update { state ->
                    state.copy(viewerCount = action.viewerCount)
                }
            }

            Preparation.Action.StartStreamClick -> {
                viewModelScope.launch {
                    analyticsManager.trackStreamStart(
                        username = mutableState.value.username,
                        viewerCount = mutableState.value.viewerCount.min
                    )
                    saveUsernameUseCase(mutableState.value.username)
                    sendEvent(Preparation.Event.OpenStream)
                }
            }

            is Preparation.Action.StreamFinished -> {
                viewModelScope.launch {
                    if (shouldAskFeedbackUseCase() && (action.durationInSeconds > 30)) {
                        setState {
                            copy(showFeedbackDialog = true)
                        }
                        saveFeedbackAskedUseCase(feedbackAsked = true)
                    }
                }
            }

            Preparation.Action.CloseFeedbackDialogClick -> {
                setState {
                    copy(showFeedbackDialog = false)
                }
            }

            Preparation.Action.GiveFeedbackClick -> {
                setState {
                    copy(showFeedbackDialog = false)
                }

            }
        }
    }

    private fun initState() {
        viewModelScope.launch {
            val imageUri = getImageUriUseCase()
            mutableState.update { state ->
                state.copy(
                    image = if (imageUri == null) {
                        ImageSource.ResId(data = R.drawable.img_default_avatar)
                    } else {
                        ImageSource.Device(data = imageUri.toUri())
                    },
                    username = getUsernameUseCase(),
                    viewerCount = getViewerCountUseCase()
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeAndSaveUsername() {
        mutableState.map { state ->
            state.username
        }.distinctUntilChanged()
            .debounce(1_000)
            .onEach { username ->
                saveUsernameUseCase(username)
            }.launchIn(viewModelScope)
    }

}