package com.bunbeauty.tiptoplive.features.intro.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base

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