package dev.stefanosansone.imagesearch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.stefanosansone.imagesearch.ui.feature.detail.ImageDetailScreen
import dev.stefanosansone.imagesearch.ui.feature.list.ImageListScreen

@Composable
fun ImageSearchNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "list",
        modifier = modifier,
    ) {
        composable("list") { ImageListScreen(navController) }
        composable("detail/{imageId}") {
            ImageDetailScreen()
        }
    }
}