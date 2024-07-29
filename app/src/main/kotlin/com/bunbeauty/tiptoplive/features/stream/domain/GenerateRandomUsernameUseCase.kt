package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.features.stream.domain.model.User
import javax.inject.Inject
import kotlin.random.Random

class GetRandomUsernameUseCase @Inject constructor() {

    operator fun invoke(): String {
        val userNumber = Random.nextInt(User.list.size)
        val user = User.list[userNumber]

        return buildString {
            val random = Random.nextInt(10)
            if (random == 0) {
                append(user.age)
                append(randomDivider)
            }
            if (Random.nextBoolean()) {
                val partLength = Random.nextInt(1, 6)
                append(user.name.take(partLength))
                append(randomDivider)
            }
            if (Random.nextBoolean()) {
                val partLength = Random.nextInt(3, 8)
                append(user.surname.take(partLength))
                append(randomDivider)
            }
            if (Random.nextBoolean()) {
                append(user.age)
                append(randomDivider)
            }
            if (length < 8) {
                val extraLetterCount = Random.nextInt(8, 12) - length
                repeat(extraLetterCount) {
                    val randomLetter = Random.nextInt(user.surname.length)
                    append(user.surname[randomLetter])
                }
            }
        }.trimEnd('.')
    }

    private val randomDivider: String
        get() {
            return when (Random.nextInt(10)) {
                0 -> "."
                in 1..3 -> ""
                else -> "_"
            }
        }
}