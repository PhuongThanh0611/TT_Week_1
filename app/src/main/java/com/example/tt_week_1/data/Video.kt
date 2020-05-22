package com.example.tt_week_1.data

data class Trailer(
    val id: Int,
    val quicktime: List<Any>,
    val youtube: List<Youtube>
)
data class Youtube(
    val name: String,
    val size: String,
    val source: String,
    val type: String
)