package dev.stefanosansone.imagesearch.ui.feature.list

import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.paging.PagingData
import dev.stefanosansone.imagesearch.MainActivity
import dev.stefanosansone.imagesearch.data.database.daos.entity.ImageEntity
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
class UserListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var mockViewModel: ImageListViewModel

    @Before
    fun setUp() {
        mockViewModel = mockk()

        val mockUsers  = List(30) {index -> ImageEntity("Patsy", "Rogers $index", "patsy.rogers@example.com", 1) }

        every { mockViewModel.getImages() } returns flowOf(PagingData.from(mockUsers))

        // Set the content for the test
        composeTestRule.activity.setContent {
            ImageListScreen(viewModel = mockViewModel)
        }
    }

    @Test
    fun userList_isDisplayed() {
        // Assert that the user list is displayed with at least one item
        composeTestRule.onNodeWithText("Patsy Rogers 0").assertIsDisplayed()
    }

    @Test
    fun userList_isPaginated() {
        // When scroll to the last element on the visible element with UserListScreen tag
        composeTestRule.onNodeWithTag("UserListScreen").performScrollToIndex(29)

        // Then assert that the last elements are displayed
        composeTestRule.onNodeWithText("Patsy Rogers 26").assertIsDisplayed()
        composeTestRule.onNodeWithText("Patsy Rogers 29").assertIsDisplayed()
    }
}
