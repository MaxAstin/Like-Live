package com.bunbeauty.fakelivestream.common.domain

interface KeyValueStorage {

    suspend fun saveImageUri(uri: String)
    suspend fun saveUsername(username: String)
    suspend fun saveViewerCountIndex(index: Int)
    suspend fun saveFeedbackAsked(feedbackAsked: Boolean)

    suspend fun getImageUri(): String?
    suspend fun getUsername(): String?
    suspend fun getViewerCountIndex(defaultValue: Int): Int
    suspend fun getFeedbackAsked(defaultValue: Boolean): Boolean

}