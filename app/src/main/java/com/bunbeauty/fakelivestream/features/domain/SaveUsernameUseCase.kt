package com.bunbeauty.fakelivestream.features.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveUsernameUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(username: String) {
        keyValueStorage.saveUsername(username = username)
    }

}