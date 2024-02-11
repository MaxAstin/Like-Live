package com.bunbeauty.fakelivestream.features.stream.domain

data class User(
    val username: String,
    val password: String,
    val email: String,
    val age: Int,
    val picture: String,
)