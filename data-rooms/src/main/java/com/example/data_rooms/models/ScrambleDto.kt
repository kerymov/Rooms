package com.example.data_rooms.models

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
