package com.bunbeauty.tiptoplive.features.intro.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveIsIntroSeenUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(isIntroViewed: Boolean) {
        return keyValueStorage.saveIsIntroViewed(isIntroViewed = isIntroViewed)
    }

}