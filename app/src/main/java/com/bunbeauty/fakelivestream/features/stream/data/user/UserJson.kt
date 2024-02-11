package com.bunbeauty.fakelivestream.features.stream.data.user

import kotlinx.serialization.Serializable

@Serializable
class UserJson(
    val results: List<ResultsJson>
)

@Serializable
class ResultsJson(
    val name: UserNameJson,
    val email: String,
    val login: UserLoginJson,
    val dob: UserDobJson,
    val picture: UserPictureJson,
)

@Serializable
class UserNameJson(
    val first: String,
    val last: String,
)

@Serializable
class UserLoginJson(
    val username: String,
    val password: String,
)

@Serializable
class UserDobJson(
    val age: Int,
)

@Serializable
class UserPictureJson(
    val large: String,
)