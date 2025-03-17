package com.example.sampleproject.ui.details

import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sampleproject.TestData
import com.example.sampleproject.util.InjectingTestApplication
import com.example.sampleproject.util.application
import com.example.sampleproject.util.withFragment
import io.mockk.Runs
import io.mockk.every
import com.example.sampleproject.R
import com.example.sampleproject.ui.common.ActionDispatcher
import com.example.sampleproject.ui.list.NAV_ARG_ID
import com.example.sampleproject.util.getString
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
@Config(application = DetailsFragmentTest.TestApplication::class)
class DetailsFragmentTest {
    class TestApplication : InjectingTestApplication() {

        val viewModel: DetailsViewModel = mockk(relaxUnitFun = true)

        override fun inject(instance: Any?) {
            (instance as DetailsFragment).viewModel = viewModel
        }
    }

    val mockViewModel = application<TestApplication>().viewModel

    @Before
    fun setUp() {
        with(mockViewModel) {
            every { title } returns MutableStateFlow(null)
            every { backgroundColor } returns MutableStateFlow(null)
            every { id } returns MutableStateFlow(null)
            every { isCompleted } returns MutableStateFlow(null)
            every { userId } returns MutableStateFlow(null)
            every { actions } returns emptyFlow()

            every { setArguments(any()) } just Runs
            every { onBackButtonClicked() } just Runs
        }
    }

    private fun withSystemUnderTest(id: Int = TestData.testTodo.id, action: DetailsFragment.() -> Unit) =
        withFragment<DetailsFragment> (args = bundleOf(NAV_ARG_ID to id), action = { action.invoke(it) })

    @Test
    fun `WHEN fragment is initialized THEN the required views are not null`() {
        withSystemUnderTest {
            with(binding) {
                assertNotNull(textTitle)
                assertNotNull(textId)
                assertNotNull(textCompleted)
                assertNotNull(textUserid)
                assertNotNull(buttonBack)
            }
        }
    }

    @Test
    fun `WHEN fragment is initialized with a certain viewModel THEN the correct fields hold the correct data`() {
        val todo = TestData.testTodo

        val titleValue = todo.title
        val backgroundValue = ContextCompat.getColor(application(), R.color.completedBackground)
        val idValue = String.format(getString(R.string.text_details_id), todo.id)
        val isCompletedValue = getString(R.string.text_details_completed)
        val userIdValue = String.format(getString(R.string.text_details_user_id), todo.userId)

        every { mockViewModel.title } returns MutableStateFlow(titleValue)
        every { mockViewModel.backgroundColor } returns MutableStateFlow(backgroundValue)
        every { mockViewModel.id } returns MutableStateFlow(idValue)
        every { mockViewModel.isCompleted } returns MutableStateFlow(isCompletedValue)
        every { mockViewModel.userId } returns MutableStateFlow(userIdValue)

        withSystemUnderTest {
            with(binding) {
                assertEquals(expected = titleValue, actual = textTitle.text)
                assertEquals(expected = backgroundValue, actual = (background.background as ColorDrawable).color)
                assertEquals(expected = idValue, actual = textId.text)
                assertEquals(expected = isCompletedValue, actual = textCompleted.text)
                assertEquals(expected = userIdValue, actual = textUserid.text)
            }
        }
    }

    @Test
    fun `WHEN an action is received to navigate up THEN the correct navigation is executed`() {
        val navController : NavController = mockk(relaxed = true)
        val actionDispatcher = ActionDispatcher<DetailsViewModel.Action>()

        every { mockViewModel.actions } returns actionDispatcher

        withSystemUnderTest {
            Navigation.setViewNavController(requireView(), navController)

            actionDispatcher.send(lifecycleScope, DetailsViewModel.Action.NavigateBack)

            verify (exactly = 1) { navController.navigateUp() }
        }
    }
}