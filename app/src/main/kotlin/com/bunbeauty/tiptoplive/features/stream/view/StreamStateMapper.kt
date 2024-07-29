package com.bunbeauty.tiptoplive.features.stream.view

import com.bunbeauty.tiptoplive.BuildConfig
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.features.stream.domain.model.Comment
import com.bunbeauty.tiptoplive.features.stream.presentation.Stream
import com.bunbeauty.tiptoplive.features.stream.view.ui.QuestionState
import com.bunbeauty.tiptoplive.features.stream.view.ui.QuestionUi
import kotlinx.collections.immutable.toImmutableList

fun Stream.State.toViewState(): ViewState {
    return ViewState(
        image = if (imageUri == null) {
            ImageSource.ResId(R.drawable.img_default_avatar)
        } else {
            ImageSource.Device(data = imageUri)
        },
        username = username,
        viewersCount = if (viewersCount < 1_000) {
            ViewersCountUi.UpToThousand(count = viewersCount.toString())
        } else {
            val thousands = viewersCount / 1_000
            val hundreds = viewersCount % 1_000 / 100
            ViewersCountUi.Thousands(
                thousands = thousands.toString(),
                hundreds = hundreds.toString(),
            )
        },
        comments = comments.map { comment ->
            comment.toCommentUi()
        }.toImmutableList(),
        reactionCount = reactionCount,
        mode = if (BuildConfig.SHOW_CAMERA) Mode.CAMERA else Mode.VIDEO,
        isCameraEnabled = isCameraEnabled,
        isCameraFront = isCameraFront,
        showJoinRequests = showJoinRequests,
        showInvite = showInvite,
        questionState = if (questionState.show) {
            if (questionState.isEmpty) {
                QuestionState.Empty
            } else {
                QuestionState.NotEmpty(
                    notAnsweredQuestions = questionState.notAnsweredQuestions.map { question ->
                        question.toQuestionUi()
                    }.toImmutableList(),
                    answeredQuestions = questionState.answeredQuestions.map { question ->
                        question.toQuestionUi()
                    }.toImmutableList(),
                )
            }
        } else {
            QuestionState.Hidden
        },
        unreadQuestionCount = questionState.unreadQuestionCount,
        selectedQuestion = questionState.selectedQuestion?.toQuestionUi(),
        showDirect = showDirect,
    )
}

private fun Comment.toCommentUi(): CommentUi {
    return CommentUi(
        picture = if (picture == null) {
            ImageSource.ResId(R.drawable.img_default_avatar)
        } else {
            ImageSource.ResName(picture)
        },
        username = username,
        text = text,
    )
}

private fun Stream.SelectableQuestion.toQuestionUi(): QuestionUi {
    return QuestionUi(
        uuid = question.uuid,
        picture = if (question.picture == null) {
            ImageSource.ResId(R.drawable.img_default_avatar)
        } else {
            ImageSource.ResName(question.picture)
        },
        username = question.username,
        text = question.text,
        isSelected = isSelected,
    )
}