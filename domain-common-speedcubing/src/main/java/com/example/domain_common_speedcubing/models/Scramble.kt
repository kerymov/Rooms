package com.example.domain_common_speedcubing.models

data class Scramble(
    val scramble: String,
    val image: Image?,
) {
    @JvmInline
    value class Image(val faces: List<Face>)

    @JvmInline
    value class Face(val colors: List<List<Int>>)
}
