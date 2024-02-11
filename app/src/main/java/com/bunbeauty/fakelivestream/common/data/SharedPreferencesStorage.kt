package com.bunbeauty.fakelivestream.common.data

import android.content.Context
import androidx.core.content.edit
import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val IMAGE_URI_KEY = "image uri"
private const val USERNAME_KEY = "username"
private const val VIEWER_COUNT_INDEX_KEY = "viewer count index"

class SharedPreferencesStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : KeyValueStorage {

    private val sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE)

    override suspend fun saveImageUri(uri: String) {
        sharedPreferences.edit {
            putString(IMAGE_URI_KEY, uri)
        }
    }

    override suspend fun saveUsername(username: String) {
        sharedPreferences.edit {
            putString(USERNAME_KEY, username)
        }
    }

    override suspend fun saveViewerCountIndex(index: Int) {
        sharedPreferences.edit {
            putInt(VIEWER_COUNT_INDEX_KEY, index)
        }
    }

    override suspend fun getImageUri(): String? {
        return sharedPreferences.getString(IMAGE_URI_KEY, null)
    }

    override suspend fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME_KEY, null)
    }

    override suspend fun getViewerCountIndex(defaultValue: Int): Int {
        return sharedPreferences.getInt(VIEWER_COUNT_INDEX_KEY, defaultValue)
    }

}