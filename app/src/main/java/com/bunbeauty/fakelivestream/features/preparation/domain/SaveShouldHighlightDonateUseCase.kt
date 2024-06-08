package com.bunbeauty.fakelivestream.features.preparation.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveShouldHighlightDonateUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(shouldHighlight: Boolean) {
        keyValueStorage.saveShouldHighlightDonate(shouldHighlight = shouldHighlight)
    }
}