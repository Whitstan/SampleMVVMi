package com.example.sampleproject.ui.details

import android.app.Application
import androidx.core.content.ContextCompat
import com.example.sampleproject.BaseTest
import com.example.sampleproject.R
import com.example.sampleproject.TestData
import com.example.sampleproject.domain.interactor.TodoInteractor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sampleproject.util.getString
import app.cash.turbine.test
import com.example.sampleproject.util.application

@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class DetailsViewModelImplTest : BaseTest() {
    private val mockContext: Application = application<Application>()
    private val mockTodoInteractor: TodoInteractor = mockk()

    private inline fun withSystemUnderTest(
        action: DetailsViewModelImpl.() -> Unit,
    ) {
        DetailsViewModelImpl(mockContext, mockTodoInteractor).apply(action)
    }

    @Before
    fun warmUp() {
        coEvery { mockTodoInteractor.getTodoById(any()) } returns TestData.testTodo
    }

    @Test
    fun `WHEN setArguments is called with a valid Todo ID THEN the corresponding fields are filled with data`() {
        testSetArguments(
            isCompletedForMockTodo = false,
            expectedIsCompletedValue = getString(R.string.text_details_not_completed),
            expectedBackgroundColor = R.color.notCompletedBackground
        )
        testSetArguments(
            isCompletedForMockTodo = true,
            expectedIsCompletedValue = getString(R.string.text_details_completed),
            expectedBackgroundColor = R.color.completedBackground
        )
    }

    private fun testSetArguments(isCompletedForMockTodo: Boolean, expectedIsCompletedValue: String, expectedBackgroundColor: Int) = runTest {
        coEvery { mockTodoInteractor.getTodoById(any()) } returns TestData.testTodo.copy(isCompleted = isCompletedForMockTodo)
        withSystemUnderTest {
            setArguments(TestData.testTodo.id)

            advanceUntilIdle()

            val expectedTodo = TestData.testTodo

            assertEquals(expected = expectedTodo.title, actual = title.value)
            assertEquals(expected = String.format(getString(R.string.text_details_id), expectedTodo.id), actual = id.value)
            assertEquals(expected = expectedIsCompletedValue, actual = isCompleted.value)
            assertEquals(expected = String.format(getString(R.string.text_details_user_id), expectedTodo.userId), actual = userId.value)
            assertEquals(expected = ContextCompat.getColor(mockContext, expectedBackgroundColor), actual = backgroundColor.value)
        }
    }

    @Test
    fun `WHEN onBackButtonClicked is called THEN an action is sent out for navigating back`() {
        withSystemUnderTest {
            onBackButtonClicked()
            runTest {
                actions.test {
                    assertEquals(DetailsViewModel.Action.NavigateBack, awaitItem())
                }
            }
        }
    }
}