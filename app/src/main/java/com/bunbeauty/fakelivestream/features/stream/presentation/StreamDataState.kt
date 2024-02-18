package com.bunbeauty.fakelivestream.features.stream.presentation

import android.net.Uri
import com.bunbeauty.fakelivestream.features.stream.domain.Comment

data class StreamDataState(
    val imageUri: Uri?,
    val username: String,
    val viewersCount: Int,
    val comments: List<Comment>,
    val reactionCount: Int,
)