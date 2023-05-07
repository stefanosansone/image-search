package dev.stefanosansone.imagesearch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.stefanosansone.imagesearch.data.database.ImageDatabase
import dev.stefanosansone.imagesearch.data.database.daos.ImageDao
import dev.stefanosansone.imagesearch.data.database.daos.entity.ImageEntity
import dev.stefanosansone.imagesearch.data.network.ApiInterface
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val query: String,
    private val database: ImageDatabase,
    private val apiInterface: ApiInterface,
    private val userDao: ImageDao
) : RemoteMediator<Int, ImageEntity>() {

    private var currentPage = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> currentPage
            }

            val response = apiInterface.getImages(page,query)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.deleteAll()
                    currentPage = 1
                }
                userDao.insertAll(
                    response.hits.map {
                        ImageEntity(
                            it.id,
                            it.previewURL,
                            it.largeImageURL,
                            it.user,
                            it.tags,
                            it.likes,
                            it.comments,
                            it.downloads
                        )
                    }
                )
            }

            currentPage++

            MediatorResult.Success(
                endOfPaginationReached = response.hits.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
