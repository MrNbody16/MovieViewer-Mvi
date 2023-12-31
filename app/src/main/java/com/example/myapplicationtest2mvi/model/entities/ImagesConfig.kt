package com.mr_nbody16.moviewviewer.models

data class ImagesConfig(
    val backdrop_sizes: List<String> = listOf(),
    val base_url: String = "",
    val logo_sizes: List<String> = listOf(),
    val poster_sizes: List<String> = listOf(),
    val profile_sizes: List<String> = listOf(),
    val secure_base_url: String = "",
    val still_sizes: List<String> = listOf()
)