package com.bunbeauty.fakelivestream.features.stream.presentation

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.bunbeauty.fakelivestream.common.analytics.AnalyticsManager
import com.bunbeauty.fakelivestream.common.presentation.BaseViewModel
import com.bunbeauty.fakelivestream.features.domain.GetImageUriFlowUseCase
import com.bunbeauty.fakelivestream.features.domain.GetUsernameUseCase
import com.bunbeauty.fakelivestream.features.domain.GetViewerCountUseCase
import com.bunbeauty.fakelivestream.features.stream.CameraUtil
import com.bunbeauty.fakelivestream.features.stream.domain.GetCommentsDelayUseCase
import com.bunbeauty.fakelivestream.features.stream.domain.GetCommentsUseCase
import com.bunbeauty.fakelivestream.features.stream.domain.GetQuestionUseCase
import com.bunbeauty.fakelivestream.features.stream.domain.model.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random

@HiltViewModel
class StreamViewModel @Inject constructor(
    private val getImageUriFlowUseCase: GetImageUriFlowUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getViewerCountUseCase: GetViewerCountUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val getCommentsDelayUseCase: GetCommentsDelayUseCase,
    private val getQuestionUseCase: GetQuestionUseCase,
    private val analyticsManager: AnalyticsManager,
    private val cameraUtil: CameraUtil,
) : BaseViewModel<Stream.State, Stream.Action, Stream.Event>(
    initState = {
        Stream.State(
            imageUri = null,
            username = "",
            viewersCount = 0,
            comments = emptyList(),
            reactionCount = 0,
            questionState = Stream.QuestionState(
                show = false,
                questions = emptyList(),
                unreadQuestionCount = null,
                currentQuestionToAnswer = null,
            ),
            startStreamTimeMillis = System.currentTimeMillis(),
            isCameraEnabled = cameraUtil.hasCamera(),
            isCameraFront = cameraUtil.hasFrontCamera(),
            showJoinRequests = false,
            showInvite = false,
            showDirect = false,
        )
    }
) {

    init {
        setupAvatar()
        setupUsername()
        setupViewerCount()

        startGenerateReactions()
        startGenerateViewersCount()
        startGenerateComments()
        startGenerateQuestions()
    }

    override fun onAction(action: Stream.Action) {
        when (action) {
            Stream.Action.SwitchCameraClick -> {
                if (mutableState.value.isCameraFront) {
                    if (cameraUtil.hasBackCamera()) {
                        setState {
                            copy(isCameraFront = false)
                        }
                    }
                } else {
                    if (cameraUtil.hasFrontCamera()) {
                        setState {
                            copy(isCameraFront = true)
                        }
                    }
                }
            }

            Stream.Action.CameraClick -> {
                if (cameraUtil.hasCamera()) {
                    setState {
                        copy(isCameraEnabled = !isCameraEnabled)
                    }
                }
            }

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
                    copy(
                        questionState = questionState.copy(
                            show = true,
                            unreadQuestionCount = null
                        )
                    )
                }
            }

            Stream.Action.HideQuestions -> {
                setState {
                    copy(
                        questionState = questionState.copy(
                            show = false,
                            currentQuestionToAnswer = questionState.selectedQuestion
                        )
                    )
                }
            }

            is Stream.Action.ClickQuestion -> {
                setState {
                    copy(
                        questionState = questionState.copy(
                            questions = questionState.questions.map { question ->
                                question.copy(
                                    isSelected = if (question.question.uuid == action.uuid) {
                                        !question.isSelected
                                    } else {
                                        false
                                    },
                                    isAnswered = question.isAnswered ||
                                        (question.question.uuid == questionState.currentQuestionToAnswer?.question?.uuid)
                                )
                            },
                            currentQuestionToAnswer = null,
                        )
                    )
                }
            }

            Stream.Action.CloseCurrentQuestion -> {
                setState {
                    copy(
                        questionState = questionState.copy(
                            questions = questionState.questions.map { question ->
                                question.copy(
                                    isSelected = false,
                                    isAnswered = question.isAnswered || question.isSelected
                                )
                            }
                        )
                    )
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

    private fun setupAvatar() {
        setState {
            copy(imageUri = getImageUriFlowUseCase().firstOrNull()?.toUri())
        }
    }

    private fun setupUsername() {
        setState {
            copy(username = getUsernameUseCase())
        }
    }

    private fun setupViewerCount() {
        viewModelScope.launch {
            setState {
                copy(viewersCount = getViewerCountUseCase().min)
            }
        }
    }

    private fun startGenerateReactions() {
        viewModelScope.launch {
            delay(5_000)
            val viewerCount = getViewerCountUseCase()
            setState {
                copy(
                    reactionCount = min(10, viewerCount.min / 100 + 1)
                )
            }
        }
    }

    private fun startGenerateViewersCount() {
        viewModelScope.launch {
            delay(1_000)

            val viewerCount = getViewerCountUseCase()
            val onePercent = (viewerCount.max - viewerCount.min) / 100
            val step = min(onePercent, Random.nextInt(200, 800))
            val median = viewerCount.min + (viewerCount.max - viewerCount.min) / 2
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
                val newComments = getCommentsUseCase(viewersCount = currentState.viewersCount)
                val delayMillis = getCommentsDelayUseCase(viewersCount = currentState.viewersCount)
                delay(delayMillis)

                setState {
                    copy(
                        comments = newComments + comments.take(100)
                    )
                }
            }
        }
    }

    private fun startGenerateQuestions() {
        viewModelScope.launch {
            delay(Random.nextLong(5_000, 15_000))
            while (true) {
                val questionCount = currentState.questionState.notAnsweredQuestions.size
                val newQuestion = getQuestionUseCase(questionCount = questionCount)
                if (newQuestion != null) {
                    handleNewQuestion(question = newQuestion)
                }

                delay(Random.nextLong(20_000, 50_000))
            }
        }
    }

    private fun handleNewQuestion(question: Question) {
        val newQuestion = Stream.SelectableQuestion(
            question = question,
            isSelected = false,
            isAnswered = false,
        )
        setState {
            copy(
                questionState = questionState.copy(
                    questions = questionState.questions + newQuestion,
                    unreadQuestionCount = (questionState.unreadQuestionCount ?: 0) + 1
                ),
            )
        }
    }

    private fun getStreamDurationInSeconds(): Int {
        val start = mutableState.value.startStreamTimeMillis
        val finish = System.currentTimeMillis()

        return (finish - start).toInt() / 1000
    }
}