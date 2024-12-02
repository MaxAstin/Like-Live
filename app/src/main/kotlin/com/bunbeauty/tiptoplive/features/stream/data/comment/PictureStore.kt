package com.bunbeauty.tiptoplive.features.stream.data.comment

private const val PICTURE_COUNT = 100

class PictureStore {

    private var nextIndex = 0
    private var pictureNames = generatePictureNames()

    fun getNext(): String {
        val comment = pictureNames[nextIndex]

        val newIndex = nextIndex + 1
        nextIndex = newIndex % pictureNames.size

        if (newIndex > pictureNames.size) {
            pictureNames = generatePictureNames()
        }

        return comment
    }

    private fun generatePictureNames(): List<String> {
        return (1..PICTURE_COUNT)
            .shuffled()
            .map { i ->
                "a$i"
            }
    }

}