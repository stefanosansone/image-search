package dev.stefanosansone.imagesearch.data.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.stefanosansone.imagesearch.data.database.daos.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ImageEntity>)

    @Query("SELECT * FROM images order by tags, id")
    fun pagingSource(): PagingSource<Int, ImageEntity>

    @Query("DELETE FROM images")
    suspend fun deleteAll()

    @Query("SELECT * FROM images WHERE id LIKE :id ")
    fun getImageById(id: String): Flow<ImageEntity>
}