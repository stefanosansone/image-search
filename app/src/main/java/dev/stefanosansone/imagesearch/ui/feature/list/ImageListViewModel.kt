package dev.stefanosansone.imagesearch.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stefanosansone.imagesearch.data.database.daos.entity.ImageEntity
import dev.stefanosansone.imagesearch.data.model.Image
import dev.stefanosansone.imagesearch.data.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val imageRepository: ImageRepository
): ViewModel() {
    fun getImages(query: String): Flow<PagingData<Image>> {
        return if (query.length >= 3) {
            imageRepository.getImages(query).cachedIn(viewModelScope)
        } else {
            flowOf(PagingData.empty())
        }
    }
}
