package com.bunbeauty.fakelivestream.features.preparation.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveFeedbackAskedUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(feedbackAsked: Boolean) {
        keyValueStorage.saveFeedbackAsked(feedbackAsked = feedbackAsked)
    }
}