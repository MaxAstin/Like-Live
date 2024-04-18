package com.bunbeauty.fakelivestream.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : Base.State, A : Base.Action, E : Base.Event>(
    initState: () -> S
) : ViewModel() {

    protected val mutableState = MutableStateFlow(initState())
    protected val currentState: S
        get() = mutableState.value
    val state: StateFlow<S> = mutableState.asStateFlow()

    private val mutableEvent = Channel<E>()
    val event: Flow<E> = mutableEvent.receiveAsFlow()

    abstract fun onAction(action: A)

    protected fun setState(block: suspend S.() -> S) {
        viewModelScope.launch {
            mutableState.update { state ->
                state.block()
            }
        }
    }

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            mutableEvent.send(event)
        }
    }

}