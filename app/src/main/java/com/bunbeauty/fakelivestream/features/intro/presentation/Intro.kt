package com.bunbeauty.fakelivestream.features.intro.presentation

import com.bunbeauty.fakelivestream.common.presentation.Base

interface Intro {

    data class State(
        val isChecked: Boolean
    ) : Base.State

    sealed interface Action : Base.Action {
        data object NextClick : Action
    }

    sealed interface Event : Base.Event {
        data object OpenPreparation : Event
    }
}