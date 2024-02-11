package com.bunbeauty.fakelivestream.features.stream.data.user

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.lang.Exception
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