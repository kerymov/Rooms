package com.example.rooms.data.model.rooms

import kotlinx.serialization.Serializable

@Serializable
data class ScrambleDto(
    val scramble: String,
    val image: Image?,
) {
    @Serializable
    data class Image(val faces: List<Face>)

    @Serializable
    data class Face(val colors: List<List<Int>>)
}
