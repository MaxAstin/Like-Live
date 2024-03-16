package com.bunbeauty.fakelivestream.features.stream.presentation

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bunbeauty.fakelivestream.common.analytics.AnalyticsManager
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.features.domain.GetImageUriUseCase
import com.bunbeauty.fakelivestream.features.domain.GetUsernameUseCase
import com.bunbeauty.fakelivestream.features.domain.GetViewerCountUseCase
import com.bunbeauty.fakelivestream.features.stream.domain.Comment
import com.bunbeauty.fakelivestream.features.stream.domain.GetCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val analyticsManager: AnalyticsManager,
    @ApplicationContext private val applicationContext: Context
) : BaseViewModel<Stream.State, Stream.Action, Stream.Event>(
    initState = {
        Stream.State(
            imageUri = null,
            username = "",
            viewersCount = 0,
            comments = emptyList(),
            reactionCount = 0,
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
                analyticsManager.trackStreamResumption()
            }

            Stream.Action.Stop -> {
                analyticsManager.trackStreamStop()
            }

            Stream.Action.FinishStreamClick -> {
                analyticsManager.trackStreamFinish()
                sendEvent(Stream.Event.GoBack)
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
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                val commentCount = Random.nextInt(1, 5)
                val newComments = getComments(count = commentCount)

                newComments.forEach { comment ->
                    preloadUserAvatar(comment)
                }
                val needPreload = newComments.any { it.picture != null }
                if (needPreload) {
                    delay(2_000)
                }

                setState {
                    copy(
                        comments = newComments + comments.take(100)
                    )
                }
            }
        }
    }

    private fun preloadUserAvatar(comment: Comment) {
        if (comment.picture == null) {
            return
        }

        val request = ImageRequest.Builder(applicationContext)
            .data(comment.picture)
            .diskCachePolicy(CachePolicy.DISABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(comment.username)
            .build()
        applicationContext.imageLoader.enqueue(request)
    }

}