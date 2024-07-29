package com.bunbeauty.tiptoplive.features.stream.data.comment

class CommentStore(private val comments: Array<String>) {

    private var nextIndex = 0

    init {
        comments.shuffle()
    }

    fun getNext(): String {
        val comment = comments[nextIndex]
        nextIndex = (nextIndex + 1) % comments.size

        return comment
    }

}