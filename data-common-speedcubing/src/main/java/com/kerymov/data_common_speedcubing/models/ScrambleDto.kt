package com.kerymov.data_common_speedcubing.models

data class ScrambleDto(
    val scramble: String,
    val image: Image?,
) {
    data class Image(val faces: List<Face>)

    data class Face(val colors: List<List<Int>>)
}
