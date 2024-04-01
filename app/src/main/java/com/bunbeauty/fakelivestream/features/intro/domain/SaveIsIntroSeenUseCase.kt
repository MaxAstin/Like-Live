package com.bunbeauty.fakelivestream.features.intro.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveIsIntroSeenUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(isIntroViewed: Boolean) {
        return keyValueStorage.saveIsIntroViewed(isIntroViewed = isIntroViewed)
    }

}