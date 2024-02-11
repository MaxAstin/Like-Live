package com.bunbeauty.fakelivestream.features.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import com.bunbeauty.fakelivestream.features.domain.model.ViewerCount
import javax.inject.Inject

class SaveViewerCountUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(viewerCount: ViewerCount) {
        keyValueStorage.saveViewerCountIndex(index = ViewerCount.entries.indexOf(viewerCount))
    }

}