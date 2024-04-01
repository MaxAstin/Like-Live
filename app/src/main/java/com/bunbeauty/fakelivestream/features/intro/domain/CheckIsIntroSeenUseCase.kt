package com.bunbeauty.fakelivestream.features.intro.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import javax.inject.Inject

class CheckIsIntroViewedUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): Boolean {
        return keyValueStorage.getIsIntroViewed( defaultValue = false)
    }

}