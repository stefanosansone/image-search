package dev.stefanosansone.imagesearch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import dev.stefanosansone.imagesearch.data.database.ImageDatabase
import dev.stefanosansone.imagesearch.data.database.daos.ImageDao
import dev.stefanosansone.imagesearch.data.database.daos.entity.ImageEntity
import dev.stefanosansone.imagesearch.data.database.daos.entity.asExternalModel
import dev.stefanosansone.imagesearch.data.model.Image
import dev.stefanosansone.imagesearch.data.network.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ImageRepository @Inject constructor(
    private val database: ImageDatabase,
    private val apiInterface: ApiInterface,
    private val imageDao: ImageDao
) {

    fun getImages(query: String) = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = ImageRemoteMediator(query, database, apiInterface, imageDao)
    ){
        database.imageDao().pagingSource()
    }.flow.map { it.map(ImageEntity::asExternalModel) }

    suspend fun getImageById(imageId: String): Image = withContext(Dispatchers.IO) {
        return@withContext imageDao.getImageById(imageId).asExternalModel()
    }
}