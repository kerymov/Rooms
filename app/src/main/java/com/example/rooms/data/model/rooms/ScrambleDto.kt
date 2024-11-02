package com.example.rooms.data.model.rooms

data class ScrambleDto(
    val scramble: String,
    val image: Image,
) {
    @JvmInline
    value class Image(val faces: List<Face>)

    @JvmInline
    value class Face(val colors: List<List<Int>>)
}
