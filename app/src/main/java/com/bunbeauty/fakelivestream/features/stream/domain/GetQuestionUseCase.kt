package com.bunbeauty.fakelivestream.features.stream.domain

import com.bunbeauty.fakelivestream.features.stream.data.user.UserRepository
import com.bunbeauty.fakelivestream.features.stream.domain.model.Question
import java.util.UUID
import javax.inject.Inject

private const val QUESTION_LIMIT = 3

class GetQuestionUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val getRandomCommentText: GetRandomCommentTextUseCase,
    private val getRandomUsernameUseCase: GetRandomUsernameUseCase,
) {

    operator fun invoke(questionCount: Int): Question? {
        if (questionCount >= QUESTION_LIMIT) {
            return null
        }

        return  Question(
            uuid = UUID.randomUUID().toString(),
            picture = userRepository.getQuestionPictureName(),
            username = getRandomUsernameUseCase(),
            text = getRandomCommentText(type = CommentType.QUESTION),
        )
    }

}