package com.bunbeauty.fakelivestream.features.stream.data.user

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import javax.inject.Inject

class RandomUserApiService @Inject constructor(
    private val client: HttpClient
) {

    suspend fun getRandomUser(): UserJson? {
        return try {
            client.get {
                url {
                    path("/")
                }
            }.body()
        } catch (exception: Exception) {
            Log.d("Network", "Exception: ${exception.message}")
            null
        }
    }

}