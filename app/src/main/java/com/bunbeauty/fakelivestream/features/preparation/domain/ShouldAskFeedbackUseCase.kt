package com.bunbeauty.fakelivestream.features.preparation.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import javax.inject.Inject

class ShouldAskFeedbackUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): Boolean {
        val feedbackAsked = keyValueStorage.getFeedbackAsked(false)

        return !feedbackAsked
    }
}