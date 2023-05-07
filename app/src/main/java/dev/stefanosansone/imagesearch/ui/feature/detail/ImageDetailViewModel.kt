package dev.stefanosansone.imagesearch.ui.feature.detail

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stefanosansone.imagesearch.data.model.Image
import dev.stefanosansone.imagesearch.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    imageRepository: ImageRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val imageId: String = savedStateHandle["imageId"] ?: ""

    private val _uiState = MutableStateFlow<ImageDetailUiState>(ImageDetailUiState.Loading)
    val uiState: StateFlow<ImageDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val image = imageRepository.getImageById(imageId)
                _uiState.value = ImageDetailUiState.Success(image)
            } catch (e: Exception) {
                Log.d(TAG,"Error : ${e.localizedMessage}")
                _uiState.value = ImageDetailUiState.Error
            }
        }
    }
}

sealed interface ImageDetailUiState {
    data class Success(val image: Image) : ImageDetailUiState
    object Error : ImageDetailUiState
    object Loading : ImageDetailUiState
}
