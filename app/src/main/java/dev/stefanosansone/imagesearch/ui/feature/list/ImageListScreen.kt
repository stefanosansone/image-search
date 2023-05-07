package dev.stefanosansone.imagesearch.ui.feature.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import dev.stefanosansone.imagesearch.data.model.Image
import dev.stefanosansone.imagesearch.ui.theme.SearchImageTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImageListScreen(
    navController: NavController,
    viewModel: ImageListViewModel = hiltViewModel()
) {
    var searchText by remember { mutableStateOf("") }
    val images = viewModel.getImages(searchText).collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            ImageListSearchBar(searchText) { searchText = it }
        }
    ) { paddingValues ->
        ImageListContent(navController,paddingValues,images,searchText)
    }
}

@Composable
private fun ImageListContent(
    navController: NavController,
    paddingValues: PaddingValues,
    images: LazyPagingItems<Image>,
    searchText: String
){
    Box(
        modifier = Modifier.padding(paddingValues).fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        if (searchText.length > 2) {
            if(images.itemCount > 0){
                LazyColumn(
                    modifier = Modifier.testTag("ImageListScreen")
                ) {
                    items(images) { image ->
                        image?.let { ImageListItem(navController,it) }
                    }
                    item { LoadStateHandler(images.loadState.refresh) }
                    item { LoadStateHandler(images.loadState.append) }
                }
            } else {
                Text(
                    text = "No results for the given query...",
                    modifier = Modifier.padding(30.dp),
                    fontStyle = FontStyle.Italic
                )
            }
        } else {
            Text(
                text = "Please enter at least 3 characters to start your search...",
                modifier = Modifier.padding(30.dp),
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageListSearchBar(
    searchText: String,
    onSearch: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        TextField(
            value = searchText,
            onValueChange = {
                onSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Search images",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.titleSmall,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch(searchText) },
            ),
        )
        IconButton(
            onClick = { onSearch(searchText) },
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageListItem(
    navController: NavController,
    image: Image
) {
    with(image) {
        ListItem(
            headlineText = { Text(user) },
            modifier = Modifier.clickable { navController.navigate("detail/${image.id}") },
            supportingText = { Text(tags) },
            leadingContent = {
                SubcomposeAsyncImage(
                    model = previewURL,
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
            }
        )
    }
    Divider()
}

@Composable
private fun LoadStateHandler(loadState: LoadState) {
    when (loadState) {
        is LoadState.Error -> {
            ErrorItem(loadState)
        }
        is LoadState.Loading -> {
            LoadingItem()
        }
        else -> {}
    }
}

@Composable
private fun LoadingItem() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(10.dp))
    }
}

@Composable
private fun ErrorItem(
    loadState: LoadState
) {
    (loadState as LoadState.Error).error.localizedMessage?.let {
        Text(text = "Error : $it", modifier = Modifier.padding(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ImageListItemPreview() {
    SearchImageTheme {
        ImageListItem(
            rememberNavController(),
            Image(123456,
                "https://cdn.pixabay.com/photo/2017/05/08/13/15/spring-bird-2295434_150.jpg",
                "https://pixabay.com/get/gc8b97b683d1f39dc8592d0e7d355bfd34c4be2d9fa9e73624ba3c9b23bc4bff3be8c98287f01b78738b80dfceb2b3db81f86b24e0de326f2cc749b0b51334f02_1280.jpg",
                "Stefano",
                "spring,bird,bird,tit",
                111,
                222,
                333
            )
        )
    }
}