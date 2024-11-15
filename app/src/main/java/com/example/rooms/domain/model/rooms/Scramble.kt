package com.example.rooms.domain.model.rooms

data class Scramble(
    val scramble: String,
    val image: Image?,
) {
    @JvmInline
    value class Image(val faces: List<Face>)

    @JvmInline
    value class Face(val colors: List<List<Int>>)
}
