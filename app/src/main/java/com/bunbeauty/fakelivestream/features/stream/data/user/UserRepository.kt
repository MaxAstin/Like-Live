package com.bunbeauty.fakelivestream.features.stream.data.user

import com.bunbeauty.fakelivestream.features.stream.domain.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val randomUserApiService: RandomUserApiService) {

    suspend fun getRandomUser(): User? {
        return randomUserApiService.getRandomUser()?.results?.firstOrNull()?.run {
            User(
                username = login.username,
                password = login.password,
                email = email,
                age = dob.age,
                picture = picture.large,
            )
        }
    }

}