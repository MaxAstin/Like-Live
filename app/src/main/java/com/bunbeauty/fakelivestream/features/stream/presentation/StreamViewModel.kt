package com.bunbeauty.fakelivestream.features.stream.presentation

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.bunbeauty.fakelivestream.common.analytics.AnalyticsManager
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.features.domain.GetImageUriUseCase
import com.bunbeauty.fakelivestream.features.domain.GetUsernameUseCase
import com.bunbeauty.fakelivestream.features.domain.GetViewerCountUseCase
import com.bunbeauty.fakelivestream.features.stream.domain.GetCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random

@HiltViewModel
class StreamViewModel @Inject constructor(
    private val getImageUriUseCase: GetImageUriUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getViewerCountUseCase: GetViewerCountUseCase,
    private val getComments: GetCommentsUseCase,
    private val analyticsManager: AnalyticsManager
) : BaseViewModel<Stream.State, Stream.Action, Stream.Event>(
    initState = {
        Stream.State(
            imageUri = null,
            username = "",
            viewersCount = 0,
            comments = emptyList(),
            reactionCount = 0,
            startStreamTimeMillis = System.currentTimeMillis(),
            showJoinRequests = false,
            showInvite = false,
            showQuestions = false,
            showDirect = false,
        )
    }
) {

    init {
        getAvatar()
        getUsername()
        getViewerCount()
    }

    override fun onAction(action: Stream.Action) {
        when (action) {
            Stream.Action.ShowJoinRequests -> {
                setState {
                    copy(showJoinRequests = true)
                }
            }

            Stream.Action.HideJoinRequests -> {
                setState {
                    copy(showJoinRequests = false)
                }
            }

            Stream.Action.ShowInvite -> {
                setState {
                    copy(showInvite = true)
                }
            }

            Stream.Action.HideInvite -> {
                setState {
                    copy(showInvite = false)
                }
            }

            Stream.Action.ShowQuestions -> {
                setState {
                    copy(showQuestions = true)
                }
            }

            Stream.Action.HideQuestions -> {
                setState {
                    copy(showQuestions = false)
                }
            }

            Stream.Action.ShowDirect -> {
                setState {
                    copy(showDirect = true)
                }
            }

            Stream.Action.HideDirect -> {
                setState {
                    copy(showDirect = false)
                }
            }

            Stream.Action.Start -> {
                setState {
                    copy(startStreamTimeMillis = System.currentTimeMillis())
                }
            }

            Stream.Action.Stop -> {
                val durationInSeconds = getStreamDurationInSeconds()
                analyticsManager.trackStreamStop(durationInSeconds = durationInSeconds)
            }

            Stream.Action.FinishStreamClick -> {
                val durationInSeconds = getStreamDurationInSeconds()
                analyticsManager.trackStreamFinish(durationInSeconds = durationInSeconds)
                sendEvent(Stream.Event.GoBack(durationInSeconds = durationInSeconds))
            }
        }
    }

    private fun getAvatar() {
        setState {
            copy(imageUri = getImageUriUseCase()?.toUri())
        }
    }

    private fun getUsername() {
        setState {
            copy(username = getUsernameUseCase())
        }
    }

    private fun getViewerCount() {
        viewModelScope.launch {
            val viewerCount = getViewerCountUseCase()
            setState {
                copy(viewersCount = viewerCount.min)
            }

            startGenerateReactions(viewerCount = viewerCount.min)
            startGenerateViewersCount(
                min = viewerCount.min,
                max = viewerCount.max
            )
            startGenerateComments()
        }
    }

    private fun startGenerateReactions(viewerCount: Int) {
        viewModelScope.launch {
            delay(5_000)
            setState {
                copy(
                    reactionCount = min(10, viewerCount / 100 + 1)
                )
            }
        }
    }

    private fun startGenerateViewersCount(min: Int, max: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            delay(1_000)

            val onePercent = (max - min) / 100
            val step = min(onePercent, Random.nextInt(200, 800))
            val median = min + (max - min) / 2
            while (true) {
                val delayMillis = Random.nextLong(2_000, 4_000)
                delay(delayMillis)

                val current = mutableState.value.viewersCount
                val random = Random.nextInt(10)
                val isPositive = if (current < median) {
                    random < 6 // 0-5 vs 6-9 - 60%
                } else {
                    random < 3 // 0-2 vs 3-9 - 30%
                }
                val change = Random.nextInt(step, step * 5)
                val newCount = if (isPositive) {
                    current + change
                } else {
                    current - change
                }
                setState {
                    copy(viewersCount = newCount)
                }
            }
        }
    }

    private fun startGenerateComments() {
        viewModelScope.launch {
            while (true) {
                val viewersCount = mutableState.value.viewersCount
                val commentCount = if (viewersCount < 1000) {
                    1
                } else {
                    val maxCommentCount = (viewersCount / 1_000).coerceIn(2, 5)
                    Random.nextInt(1, maxCommentCount)
                }

                val newComments = getComments(count = commentCount)

                val delayMillis = if (viewersCount < 1000) {
                    Random.nextLong(1_000, 2_000)
                } else {
                    Random.nextLong(500, 1_000)
                }
                delay(delayMillis)

                setState {
                    copy(
                        comments = newComments + comments.take(100)
                    )
                }
            }
        }
    }

    private fun getStreamDurationInSeconds(): Int {
        val start = mutableState.value.startStreamTimeMillis
        val finish = System.currentTimeMillis()

        return (finish - start).toInt() / 1000
    }
}