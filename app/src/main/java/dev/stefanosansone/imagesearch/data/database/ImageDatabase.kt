package dev.stefanosansone.imagesearch.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.stefanosansone.imagesearch.data.database.daos.ImageDao
import dev.stefanosansone.imagesearch.data.database.daos.entity.ImageEntity

@Database(entities = [ImageEntity::class], version = 1, exportSchema = false)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}
