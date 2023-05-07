package dev.stefanosansone.imagesearch.data.model

data class Image(
    val id: Int,
    val previewURL: String,
    val largeImageURL: String,
    val user: String,
    val tags: String,
    val likes: Int,
    val comments: Int,
    val downloads: Int
)