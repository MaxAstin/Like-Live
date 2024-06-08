package com.bunbeauty.fakelivestream.common.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MultipleEventsCutter(
    private val actionState: MutableSharedFlow<() -> Unit>
) {
    fun processEvent(onEvent: () -> Unit) {
        actionState.tryEmit(onEvent)
    }
}

@OptIn(FlowPreview::class)
@Composable
fun rememberMultipleEventsCutter(): MultipleEventsCutter {
    val actionState = remember {
        MutableSharedFlow<() -> Unit>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }

    val multipleEventsCutter = remember {
        MultipleEventsCutter(actionState)
    }

    LaunchedEffect(Unit) {
        actionState
            .debounce(300)
            .onEach { onEvent ->
                onEvent()
            }.launchIn(this)
    }

    return multipleEventsCutter
}