package com.bunbeauty.fakelivestream.features.stream.presentation

import android.net.Uri
import com.bunbeauty.fakelivestream.common.presentation.Base
import com.bunbeauty.fakelivestream.features.stream.domain.model.Comment
import com.bunbeauty.fakelivestream.features.stream.domain.model.Question

interface Stream {

    data class State(
        val imageUri: Uri?,
        val username: String,
        val viewersCount: Int,
        val comments: List<Comment>,
        val reactionCount: Int,
        val questionState: QuestionState,
        val startStreamTimeMillis: Long,
        val isCameraEnabled: Boolean,
        val isCameraFront: Boolean,
        val showJoinRequests: Boolean,
        val showInvite: Boolean,
        val showDirect: Boolean,
    ) : Base.State

    data class QuestionState(
        val show: Boolean,
        val newQuestions: List<SelectableQuestion>,
        val answeredQuestions: List<SelectableQuestion>,
        val unreadQuestionCount: Int?,
    ) {
        val isEmpty: Boolean = newQuestions.isEmpty() && answeredQuestions.isEmpty()

        val selectedQuestion: SelectableQuestion? = run {
            val newSelectedQuestion = newQuestions.find { question ->
                question.isSelected
            }
            val answeredSelectedQuestion = answeredQuestions.find { question ->
                question.isSelected
            }

            newSelectedQuestion ?: answeredSelectedQuestion
        }
    }

    data class SelectableQuestion(
        val question: Question,
        val isSelected: Boolean,
    )

    sealed interface Action : Base.Action {
        data object CameraClick : Action
        data object SwitchCameraClick : Action
        data object ShowJoinRequests : Action
        data object HideJoinRequests : Action
        data object ShowInvite : Action
        data object HideInvite : Action
        data object ShowQuestions : Action
        data object HideQuestions : Action
        data class ClickQuestion(val uuid: String) : Action
        data object CloseCurrentQuestion : Action
        data object ShowDirect : Action
        data object HideDirect : Action
        data object Start : Action
        data object Stop : Action
        data object FinishStreamClick : Action
    }

    sealed interface Event : Base.Event {
        data class GoBack(val durationInSeconds: Int) : Event
    }
}