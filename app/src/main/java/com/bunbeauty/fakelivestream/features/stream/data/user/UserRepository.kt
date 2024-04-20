package com.bunbeauty.fakelivestream.features.stream.data.user

import com.bunbeauty.fakelivestream.features.stream.data.comment.PictureStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {

    private val commentPictureStore = PictureStore()
    private val questionPictureStore = PictureStore()

    fun getCommentPictureName(): String {
        return commentPictureStore.getNext()
    }

    fun getQuestionPictureName(): String {
        return questionPictureStore.getNext()
    }

}