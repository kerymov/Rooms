package com.example.rooms.presentation.features.main.rooms.models

data class ScrambleUi(
    val scramble: String,
    val image: Image,
) {
    @JvmInline
    value class Image(val faces: List<Face>)

    @JvmInline
    value class Face(val colors: List<List<Int>>)
}
