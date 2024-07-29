package com.bunbeauty.tiptoplive.features.stream.domain

import javax.inject.Inject
import kotlin.random.Random

class GetCommentsDelayUseCase @Inject constructor() {

    operator fun invoke(viewersCount: Int): Long {
        return if (viewersCount < 1000) {
            Random.nextLong(1_000, 2_000)
        } else {
            Random.nextLong(500, 1_000)
        }
    }

}