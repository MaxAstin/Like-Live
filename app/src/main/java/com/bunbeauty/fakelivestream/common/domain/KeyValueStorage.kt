package com.bunbeauty.fakelivestream.common.domain

import kotlinx.coroutines.flow.Flow

interface KeyValueStorage {

    suspend fun saveImageUri(uri: String)
    suspend fun saveUsername(username: String)
    suspend fun saveViewerCountIndex(index: Int)
    suspend fun saveFeedbackShouldBeAsked(shouldBeAsked: Boolean)

    fun getImageUriFlow(): Flow<String?>
    suspend fun getUsername(): String?
    suspend fun getViewerCountIndex(defaultValue: Int): Int
    suspend fun getFeedbackShouldBeAsked(defaultValue: Boolean): Boolean

}