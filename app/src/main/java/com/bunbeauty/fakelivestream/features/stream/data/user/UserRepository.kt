package com.bunbeauty.fakelivestream.features.stream.data.user

import com.bunbeauty.fakelivestream.features.stream.data.comment.PictureStore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class UserRepository @Inject constructor() {

    private val pictureStore = PictureStore()

    fun getPictureName(): String? {
        return if (Random.nextInt(10) == 0) {
            null
        } else {
            pictureStore.getNext()
        }
    }

}