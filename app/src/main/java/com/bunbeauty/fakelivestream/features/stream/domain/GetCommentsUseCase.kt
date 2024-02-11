package com.bunbeauty.fakelivestream.features.stream.domain

import com.bunbeauty.fakelivestream.features.stream.data.user.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlin.random.Random

class GetCommentsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val getRandomCommentText: GetRandomCommentTextUseCase,
) {

    suspend operator fun invoke(count: Int): List<Comment> = coroutineScope {
        List(count) {
            async { userRepository.getRandomUser() }
        }.awaitAll()
            .mapNotNull { user ->
                user?.let {
                    val picture = when (Random.nextInt(10)) {
                        in 0..4 -> user.picture
                        in 5..6 -> "https://random.imagecdn.app/100/100"
                        in 7..8 -> "https://picsum.photos/100"
                        else -> null
                    }
                    Comment(
                        picture = picture,
                        username = getUsername(user),
                        text = getRandomCommentText(),
                    )
                }
            }
    }

    private fun getUsername(user: User): String {
        return user.run {
            when (Random.nextInt(16)) {
                0 -> username
                1 -> {
                    val part1 = firstNameEnglish.take(8).lowercase()
                    val part2 = lastNameEnglish.take(8).lowercase()
                    "${part1}_${part2}${age}"
                }
                2 -> {
                    val part1Length = Random.nextInt(3, 5)
                    val part1 = firstNameEnglish.take(part1Length).lowercase()
                    val part2Length = Random.nextInt(3, 6)
                    val part2 = lastNameEnglish.take(part2Length).lowercase()
                    val part3Length = Random.nextInt(2, 4)
                    val part3 = password.take(part3Length).lowercase()

                    "${part1}_${part2}_${part3}"
                }
                3 -> {
                    val part1 = firstNameEnglish.take(8).lowercase()
                    val part2 = lastNameEnglish.take(8).lowercase()
                    val part3 = password.take(8).lowercase()
                    "${part1}_${part2}$part3"
                }
                4 -> {
                    val part1 = firstNameEnglish.take(8).lowercase()
                    val part2 = lastNameEnglish.take(8).lowercase()
                    val part3 = password.take(8).lowercase()

                    "${part1}.${part2}${part3}"
                }
                5 -> {
                    val part1 = firstNameEnglish.take(8).lowercase()
                    val part2 = lastNameEnglish.take(8).lowercase()
                    val part3Length = Random.nextInt(2, 5)
                    val part3 = password.take(part3Length).lowercase()

                    "${part1}.${part2}.${part3}"
                }
                6 -> {
                    val part1Length = Random.nextInt(4, 9)
                    val part1 = firstNameEnglish.take(part1Length).lowercase()
                    val part2Length = Random.nextInt(4, 8)
                    val part2 = lastNameEnglish.take(part2Length).lowercase()

                    "${part1}_${part2}"
                }
                7 -> {
                    val part1Length = Random.nextInt(4, 9)
                    val part1 = firstNameEnglish.take(part1Length).lowercase()
                    val part2Length = Random.nextInt(4, 7)
                    val part2 = lastNameEnglish.take(part2Length).lowercase()

                    "${part1}${part2}"
                }
                8 -> {
                    val middlePart = firstNameEnglish.take(8).lowercase()
                    val random = Random.nextInt(3, 10)

                    "${age}_${middlePart}_${age + random}"
                }
                9 -> {
                    val part1Length = Random.nextInt(5, 10)
                    val part1 = firstNameEnglish.take(part1Length).lowercase()
                    val part2 = Random.nextInt(1980, 2010)

                    "${part1}_${part2}"
                }
                else -> {
                    val part1Length = Random.nextInt(4, 7)
                    val part1 = firstNameEnglish.take(part1Length).lowercase()
                    val part2 = Random.nextInt(1, 10)
                    val part3Length = Random.nextInt(2, 4)
                    val part3 = lastNameEnglish.take(part3Length).lowercase()

                    "${part1}${part2}.${part3}"
                }
            }
        }
    }

    private val User.firstNameEnglish: String
        get() = email.split('.').first()

    private val User.lastNameEnglish: String
        get() = email.split('.', '@')[1]

}