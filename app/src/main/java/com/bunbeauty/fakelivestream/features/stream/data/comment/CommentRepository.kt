package com.bunbeauty.fakelivestream.features.stream.data.comment

import android.content.Context
import android.content.res.Resources
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.features.stream.domain.CommentType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val resources = context.resources
    private val oneLetterComments = CommentStore(comments = resources.getStringArray(R.array.one_letter_comments))
    private val twoLetterComments = CommentStore(comments = resources.getStringArray(R.array.two_letter_comments))
    private val threeLetterComments = CommentStore(comments = resources.getStringArray(R.array.three_letter_comments))
    private val oneWordComments = CommentStore(comments = resources.getStringArray(R.array.one_word_comment))
    private val longComments = CommentStore(comments = resources.getStringArray(R.array.long_comments))
    private val questionComments = CommentStore(comments = resources.getStringArray(R.array.question_comments))

    fun getNextComment(type: CommentType): String {
        val comments = when (type) {
            CommentType.ONE_LETTER -> oneLetterComments
            CommentType.TWO_LETTER -> twoLetterComments
            CommentType.THREE_LETTER -> threeLetterComments
            CommentType.ONE_WORD -> oneWordComments
            CommentType.LONG -> longComments
            CommentType.QUESTION -> questionComments
        }
        return comments.getNext()
    }

}