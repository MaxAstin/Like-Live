package com.bunbeauty.fakelivestream.features.stream.domain

import com.bunbeauty.fakelivestream.features.stream.data.EMOJI
import com.bunbeauty.fakelivestream.features.stream.data.comment.CommentRepository
import javax.inject.Inject
import kotlin.random.Random

class GetRandomCommentTextUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {

    operator fun invoke(type: CommentType): String {
        return buildTextComment(
            type = type,
            withEmoji = false
        )
    }

    operator fun invoke(): String {
        return when (Random.nextInt(CommentType.entries.size + 2)) {
            0 -> buildEmojiString(maxCount = 5)
            1 -> buildSymbolString()
            else -> buildTextComment(type = randomType())
        }
    }

    private fun buildEmojiString(maxCount: Int): String {
        val count = Random.nextInt(1, maxCount + 1)
        return if (Random.nextBoolean()) {
            randomEmoji().repeat(count)
        } else {
            buildString {
                repeat(count) {
                    append(randomEmoji())
                }
            }
        }
    }

    private fun buildTextComment(
        type: CommentType,
        withEmoji: Boolean = true,
    ): String {
        var comment = commentRepository.getNextComment(type)
        comment = when (type) {
            CommentType.ONE_LETTER,
            CommentType.TWO_LETTER,
            CommentType.THREE_LETTER,
            -> {
                if (Random.nextBoolean()) {
                    comment.repeatRandomLetter()
                } else {
                    comment
                }.randomCase()
                    .withRandomSpecialSymbols()
            }

            CommentType.ONE_WORD -> {
                comment.repeatFirstOrLastLetter()
                    .randomCase()
                    .withRandomSpecialSymbols()
            }

            else -> {
                comment.lowercaseOrCapitalize()
            }
        }

        return if (withEmoji && Random.nextBoolean()) {
            comment.addRandomEmoji()
        } else {
            comment
        }
    }

    private fun String.repeatRandomLetter(): String {
        val letterIndex = Random.nextInt(length)
        val count = Random.nextInt(2, 10)
        val repeatedLetter = this[letterIndex].toString().repeat(count)
        return replaceRange(letterIndex..letterIndex, repeatedLetter)
    }

    private fun String.repeatFirstOrLastLetter(): String {
        val letterIndex = if (Random.nextBoolean()) {
            0
        } else {
            lastIndex
        }
        val count = Random.nextInt(1, 6)
        val repeatedLetter = this[letterIndex].toString().repeat(count)
        return replaceRange(letterIndex..letterIndex, repeatedLetter)
    }

    private fun String.randomCase(): String {
        return when (Random.nextInt(10)) {
            in 0..5 -> {
                replaceFirstChar { char ->
                    if (char.isLowerCase()) {
                        char.titlecase()
                    } else {
                        char.toString()
                    }
                }
            }

            in 6..8 -> lowercase()
            else -> uppercase()
        }
    }

    private fun String.withRandomSpecialSymbols(): String {
        return when (Random.nextInt(10)) {
            0 -> "$this${buildSymbolString()}"
            else -> this
        }
    }

    private fun buildSymbolString(): String {
        return when (Random.nextInt(18)) {
            in 0..5 -> ")"
            in 6..12 -> "+"
            13 -> "!"
            14 -> "."
            15 -> "*"
            16 -> "*"
            else -> "("
        }.repeat(Random.nextInt(1, 4))
    }

    private fun String.lowercaseOrCapitalize(): String {
        return if (Random.nextBoolean()) {
            replaceFirstChar { char ->
                if (char.isLowerCase()) {
                    char.titlecase()
                } else {
                    char.toString()
                }
            }
        } else {
            lowercase()
        }
    }

    private fun String.addRandomEmoji(): String {
        val emoji = buildEmojiString(maxCount = 3)
        val singleEmoji = randomEmoji()

        return when (Random.nextInt(5)) {
            0 -> "$emoji$this"
            1 -> "$this$emoji"
            2 -> "$singleEmoji$this$singleEmoji"
            3 -> "${randomEmoji()} $this ${randomEmoji()}"
            else -> "$this $singleEmoji"
        }
    }

    private fun randomEmoji(): String {
        return EMOJI[Random.nextInt(EMOJI.size)]
    }

    private fun randomType(): CommentType {
        val randomTypeIndex = Random.nextInt(CommentType.entries.size)
        return CommentType.entries[randomTypeIndex]
    }
}