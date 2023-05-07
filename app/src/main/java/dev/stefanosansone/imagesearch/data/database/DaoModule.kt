package dev.stefanosansone.imagesearch.data.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.stefanosansone.imagesearch.data.database.daos.ImageDao

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun providesUserDao(
        database: ImageDatabase,
    ): ImageDao = database.imageDao()
}