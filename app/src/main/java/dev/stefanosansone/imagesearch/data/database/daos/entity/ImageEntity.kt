package dev.stefanosansone.imagesearch.data.database.daos.entity

import androidx.room.Entity
import dev.stefanosansone.imagesearch.data.model.Image

@Entity(tableName = "images", primaryKeys = ["id"])
data class ImageEntity(
    val id: Int,
    val previewURL: String,
    val largeImageURL: String,
    val user: String,
    val tags: String,
    val likes: Int,
    val comments: Int,
    val downloads: Int
)

fun ImageEntity.asExternalModel() = Image(
    id = id,
    previewURL = previewURL,
    largeImageURL = largeImageURL,
    user = user,
    tags = tags,
    likes = likes,
    comments = comments,
    downloads = downloads
)