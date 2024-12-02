package com.bunbeauty.tiptoplive.features.preparation.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveShouldHighlightDonateUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(shouldHighlight: Boolean) {
        keyValueStorage.saveShouldHighlightDonate(shouldHighlight = shouldHighlight)
    }
}