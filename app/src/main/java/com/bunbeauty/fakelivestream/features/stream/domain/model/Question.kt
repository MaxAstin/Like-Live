package com.bunbeauty.fakelivestream.features.stream.domain.model

data class Question(
    val uuid: String,
    val picture: String?,
    val username: String,
    val text: String,
)