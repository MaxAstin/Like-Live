package com.bunbeauty.fakelivestream.features.stream.view

import com.bunbeauty.fakelivestream.BuildConfig
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.features.stream.presentation.Stream
import com.bunbeauty.fakelivestream.common.ui.components.ImageSource
import com.bunbeauty.fakelivestream.features.stream.domain.model.Comment
import com.bunbeauty.fakelivestream.features.stream.domain.model.Question
import com.bunbeauty.fakelivestream.features.stream.view.ui.QuestionState
import com.bunbeauty.fakelivestream.features.stream.view.ui.QuestionUi
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
        },
        reactionCount = reactionCount,
        mode = if (BuildConfig.SHOW_CAMERA) Mode.CAMERA else Mode.VIDEO,
        isCameraEnabled = isCameraEnabled,
        isCameraFront = isCameraFront,
        showJoinRequests = showJoinRequests,
        showInvite = showInvite,
        questionState = if (showQuestions) {
            if (questions.isEmpty()) {
                QuestionState.Empty
            } else {
                QuestionState.NotEmpty(
                    questions = questions.map { question ->
                        question.toQuestionUi()
                    }.toImmutableList()
                )
            }
        } else {
            QuestionState.Hidden
        },
        unreadQuestionCount = unreadQuestionCount,
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

private fun Question.toQuestionUi(): QuestionUi {
    return QuestionUi(
        uuid = uuid,
        picture = if (picture == null) {
            ImageSource.ResId(R.drawable.img_default_avatar)
        } else {
            ImageSource.ResName(picture)
        },
        username = username,
        text = text,
    )
}