package com.example.rooms.presentation.features.main.rooms.models

import kotlinx.serialization.Serializable

@Serializable
data class ScrambleUi(
    val scramble: String,
    val image: Image,
) {
    @Serializable
    @JvmInline
    value class Image(val faces: List<Face>)

    @Serializable
    @JvmInline
    value class Face(val colors: List<List<Int>>)
}
