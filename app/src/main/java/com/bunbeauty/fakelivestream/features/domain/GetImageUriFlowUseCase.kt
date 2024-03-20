package com.bunbeauty.fakelivestream.features.domain

import com.bunbeauty.fakelivestream.common.domain.KeyValueStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImageUriFlowUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    operator fun invoke(): Flow<String?> {
        return keyValueStorage.getImageUriFlow()
    }

}