package com.bunbeauty.fakelivestream.common.domain

interface KeyValueStorage {

    suspend fun saveImageUri(uri: String)
    suspend fun saveUsername(username: String)
    suspend fun saveViewerCountIndex(index: Int)

    suspend fun getImageUri(): String?
    suspend fun getUsername(): String?
    suspend fun getViewerCountIndex(defaultValue: Int): Int

}