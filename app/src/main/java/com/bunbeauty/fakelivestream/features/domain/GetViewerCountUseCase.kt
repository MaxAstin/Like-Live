package com.bunbeauty.fakelivestream.features.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import com.bunbeauty.fakelivestream.features.domain.model.ViewerCount
import javax.inject.Inject

class GetViewerCountUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): ViewerCount {
        val viewerCountIndex = keyValueStorage.getViewerCountIndex(defaultValue = 0)
        return ViewerCount.entries.getOrNull(viewerCountIndex) ?: ViewerCount.V_100_200
    }

}