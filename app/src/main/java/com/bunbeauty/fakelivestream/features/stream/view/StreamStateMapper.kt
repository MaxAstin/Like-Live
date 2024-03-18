package com.bunbeauty.fakelivestream.features.stream.view

import com.bunbeauty.fakelivestream.BuildConfig
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.features.stream.presentation.Stream
import com.bunbeauty.fakelivestream.common.ui.components.ImageSource

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
            CommentUi(
                picture = if (comment.picture == null) {
                    ImageSource.ResId(R.drawable.img_default_avatar)
                } else {
                    ImageSource.ResName(comment.picture)
                },
                username = comment.username,
                text = comment.text,
            )
        },
        reactionCount = reactionCount,
        showCamera = BuildConfig.SHOW_CAMERA,
        showJoinRequests = showJoinRequests,
        showInvite = showInvite,
        showQuestions = showQuestions,
        showDirect = showDirect,
    )
}