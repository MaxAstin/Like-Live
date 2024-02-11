package com.bunbeauty.fakelivestream.features.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveImageUriUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(uri: String) {
        keyValueStorage.saveImageUri(uri = uri)
    }

}