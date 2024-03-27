package com.bunbeauty.fakelivestream.features.preparation.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveShouldAskFeedbackUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(shouldAsk: Boolean) {
        keyValueStorage.saveShouldAskFeedback(shouldAsk = shouldAsk)
    }
}