package com.bunbeauty.tiptoplive.features.intro.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class CheckIsIntroViewedUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): Boolean {
        return keyValueStorage.getIsIntroViewed( defaultValue = false)
    }

}