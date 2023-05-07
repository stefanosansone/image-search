package dev.stefanosansone.imagesearch.ui.feature.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stefanosansone.imagesearch.data.database.daos.entity.ImageEntity
import dev.stefanosansone.imagesearch.data.database.daos.entity.asExternalModel
import dev.stefanosansone.imagesearch.data.model.Image
import dev.stefanosansone.imagesearch.data.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    imageRepository: ImageRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    val uiState: StateFlow<ImageDetailUiState> = imageRepository.getImageById(savedStateHandle["imageId"]?: "").map {
        //Log.d("stef",it.toString())
        ImageDetailUiState.Success(
            it.asExternalModel()
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ImageDetailUiState.Loading,
    )

}

sealed interface ImageDetailUiState {
    data class Success(val image: Image) : ImageDetailUiState
    object Error : ImageDetailUiState
    object Loading : ImageDetailUiState
}
