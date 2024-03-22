package com.bunbeauty.fakelivestream.features.preparation.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveFeedbackShouldBeAskedUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(shouldBeAsked: Boolean) {
        keyValueStorage.saveFeedbackShouldBeAsked(shouldBeAsked = shouldBeAsked)
    }
}