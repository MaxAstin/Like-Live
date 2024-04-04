package com.bunbeauty.fakelivestream.features.intro.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.features.intro.domain.CheckIsIntroViewedUseCase
import com.bunbeauty.fakelivestream.features.intro.domain.SaveIsIntroSeenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val checkIsIntroViewedUseCase: CheckIsIntroViewedUseCase,
    private val saveIsIntroSeenUseCase: SaveIsIntroSeenUseCase,
) : BaseViewModel<Intro.State, Intro.Action, Intro.Event>(
    initState = {
        Intro.State(isChecked = false)
    }
) {

    init {
        viewModelScope.launch {
            if (checkIsIntroViewedUseCase()) {
                sendEvent(Intro.Event.OpenPreparation)
            } else {
                setState {
                    copy(isChecked = true)
                }
            }
        }
    }

    override fun onAction(action: Intro.Action) {
        when (action) {
            Intro.Action.NextClick -> {
                viewModelScope.launch {
                    saveIsIntroSeenUseCase(isIntroViewed = true)
                    sendEvent(Intro.Event.OpenPreparation)
                }
            }
        }
    }

}