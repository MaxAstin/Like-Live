package com.bunbeauty.fakelivestream.features.stream.domain

import com.bunbeauty.fakelivestream.features.stream.data.user.UserRepository
import com.bunbeauty.fakelivestream.features.stream.domain.model.Comment
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val getRandomCommentText: GetRandomCommentTextUseCase,
    private val getRandomUsernameUseCase: GetRandomUsernameUseCase,
) {

    operator fun invoke(count: Int): List<Comment> {
        return List(count) {
            Comment(
                picture = userRepository.getPictureName(),
                username = getRandomUsernameUseCase(),
                text = getRandomCommentText(),
            )
        }
    }

}