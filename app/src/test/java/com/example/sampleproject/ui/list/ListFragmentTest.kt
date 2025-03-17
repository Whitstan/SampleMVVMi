package com.example.sampleproject.ui.list

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sampleproject.R
import com.example.sampleproject.TestData
import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.ui.common.ActionDispatcher
import com.example.sampleproject.ui.details.DetailsViewModel
import com.example.sampleproject.util.InjectingTestApplication
import com.example.sampleproject.util.application
import com.example.sampleproject.util.assertContains
import com.example.sampleproject.util.getCell
import com.example.sampleproject.util.getString
import com.example.sampleproject.util.withFragment
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
@Config(application = ListFragmentTest.TestApplication::class)
class ListFragmentTest {
    class TestApplication : InjectingTestApplication() {

        val viewModel: ListViewModel = mockk(relaxUnitFun = true)

        override fun inject(instance: Any?) {
            (instance as ListFragment).viewModel = viewModel
        }
    }

    val mockViewModel = application<TestApplication>().viewModel

    @Before
    fun setUp() {
        with(mockViewModel) {
            every { todos } returns MutableStateFlow(TestData.testTodoList)
            every { isLoading } returns MutableStateFlow(false)
            every { showEmptyListText } returns MutableStateFlow(false)
            every { todos } returns MutableStateFlow(TestData.testTodoList)
            every { fetchData() } just Runs
        }
    }

    private fun withSystemUnderTest(action: ListFragment.() -> Unit) =
        withFragment<ListFragment> { action.invoke(it) }

    @Test
    fun `WHEN fragment is initialized THEN the required views are not null`() {
        withSystemUnderTest {
            with(binding) {
                assertNotNull(swipeRefreshLayout)
                assertNotNull(recyclerView)
                assertNotNull(progressBarLayout)
                assertNotNull(textNoItemsFound)
            }
        }
    }

    @Test
    fun `WHEN viewModel's isLoading property is true THEN progressBarLayout is visible OTHERWISE it is not`() {
        testProgressBarVisibility(isLoadingValue = true, expectedVisibility = View.VISIBLE)
        testProgressBarVisibility(isLoadingValue = false, expectedVisibility = View.GONE)
    }

    private fun testProgressBarVisibility(isLoadingValue: Boolean, expectedVisibility: Int) {
        every { mockViewModel.isLoading } returns MutableStateFlow(isLoadingValue)
        withSystemUnderTest {
            assertEquals(
                expected = expectedVisibility,
                actual = binding.progressBarLayout.visibility
            )
        }
    }

    @Test
    fun `WHEN viewModel's showEmptyListText property is true THEN text_no_items_found is visible OTHERWISE it is not`() {
        testNoItemsTextVisibility(showEmptyListText = true, expectedVisibility = View.VISIBLE)
        testNoItemsTextVisibility(showEmptyListText = false, expectedVisibility = View.GONE)
    }

    private fun testNoItemsTextVisibility(showEmptyListText: Boolean, expectedVisibility: Int) {
        every { mockViewModel.showEmptyListText } returns MutableStateFlow(showEmptyListText)
        withSystemUnderTest {
            assertEquals(
                expected = expectedVisibility,
                actual = binding.textNoItemsFound.visibility
            )
        }
    }

    @Test
    fun `WHEN a list of Todo items loaded into adapter THEN the RecyclerView should show them correctly`() {
        withSystemUnderTest {
            binding.recyclerView.verifyListItems(TestData.testTodoList)
        }

    }

    private fun RecyclerView.verifyListItems(expected: List<Todo>) {
        assertContains(expected) { todo: Todo, viewHolder: ListItemAdapter.ItemViewHolder ->
            viewHolder.apply {
                assertEquals(expected = String.format(getString(R.string.text_list_item_id), todo.id), actual = binding.textId.text)
                assertEquals(expected = String.format(getString(R.string.text_list_item_title), todo.title), actual = binding.textTitle.text)
                assertEquals(
                    expected = ContextCompat.getColor(application(),
                        if (todo.isCompleted) R.color.completedBackground
                        else R.color.notCompletedBackground
                    ),
                    actual = binding.card.cardBackgroundColor.defaultColor
                )
            }
        }
    }

    @Test
    fun `WHEN clicked on a list item THEN the correct navigation is executed`() {
        val navController : NavController = mockk(relaxed = true)
        withSystemUnderTest {
            Navigation.setViewNavController(requireView(), navController)

            val holderForFirstElement = (binding.recyclerView.getCell(0) as ListItemAdapter.ItemViewHolder)

            holderForFirstElement.binding.card.performClick()

            val todoId = mockViewModel.todos.value[holderForFirstElement.bindingAdapterPosition].id

            verify (exactly = 1) {
                navController.navigate(
                    R.id.action_listFragment_to_detailsFragment,
                    match { bundle ->
                        bundle.getInt(NAV_ARG_ID) == todoId
                    }
                )
            }
        }
    }

    @Test
    fun `WHEN an action is received to show a snackBar THEN the correct snackBar is shown`() {
        val message = "SnackBarMessage"
        val actionDispatcher = ActionDispatcher<ListViewModel.Action>()

        every { mockViewModel.actions } returns actionDispatcher

        withSystemUnderTest {
            actionDispatcher.send(lifecycleScope, ListViewModel.Action.ShowSnackBar(message))
            onView(withText(message)).check(matches(isDisplayed()))
        }
    }

    // TODO: fixme
    /*
    @Test
    fun `WHEN swipe gesture is initiated on the swipeRefreshLayout THEN the viewModel's corresponding refresh function is called`() {
        withSystemUnderTest {
            onView(withId(R.id.swipeRefreshLayout)).perform((swipeDown()))
            verify (exactly = 1) { mockViewModel.fetchData() }
        }
    }
    */

}