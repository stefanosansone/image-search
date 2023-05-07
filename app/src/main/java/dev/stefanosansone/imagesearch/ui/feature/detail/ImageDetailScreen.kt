package dev.stefanosansone.imagesearch.ui.feature.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import dev.stefanosansone.imagesearch.data.model.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImageDetailScreen(
    viewModel: ImageDetailViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        when(uiState){
            is ImageDetailUiState.Success -> ImageDetailContent(paddingValues,
                (uiState as ImageDetailUiState.Success).image)
            else -> { }
        }
    }
}

@Composable
private fun ImageDetailContent(
    paddingValues: PaddingValues,
    image: Image
){
    Box(
    modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize(),
    contentAlignment = Alignment.TopCenter
    ) {
        Column() {
            SubcomposeAsyncImage(
                model = image.largeImageURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    CircularProgressIndicator()
                } else {
                    SubcomposeAsyncImageContent()
                }
            }
            Text(text = image.tags)
        }

    }
}