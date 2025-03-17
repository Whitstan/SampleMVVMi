package com.example.sampleproject.ui.list

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.sampleproject.BaseTest
import com.example.sampleproject.R
import com.example.sampleproject.TestData
import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.domain.interactor.TodoInteractor
import com.example.sampleproject.ui.details.DetailsViewModel
import com.example.sampleproject.util.application
import com.example.sampleproject.util.getString
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelImplTest : BaseTest() {
    private val mockContext: Application = application<Application>()
    private val mockTodoInteractor: TodoInteractor = mockk()
    private val fakeTodosDataSource = MutableStateFlow<List<Todo>>(emptyList())

    private inline fun withSystemUnderTest(
        action: ListViewModelImpl.() -> Unit,
    ) {
        ListViewModelImpl(mockContext,  mockTodoInteractor).apply(action)
    }

    @Before
    fun warmUp() {
        coEvery { mockTodoInteractor.fetchAndSaveTodos() } just Runs
        coEvery { mockTodoInteractor.todos } returns fakeTodosDataSource
    }

    @Test
    fun `WHEN fetchData is called THEN the interactor's correct method is called AND the loader statuses are correct`() {
        coEvery { mockTodoInteractor.fetchAndSaveTodos() } coAnswers { delay(100) }

        withSystemUnderTest {
            runTest {
                fetchData()
                advanceTimeBy(50)
                assertTrue(isLoading.value)
                advanceTimeBy(51)
                assertFalse(isLoading.value)
            }
        }
    }

    @Test
    fun `WHEN fetchData is called AND an exception is thrown THEN an action is sent out to show a snackBar`() {
        coEvery { mockTodoInteractor.fetchAndSaveTodos() } throws UnknownHostException()

        withSystemUnderTest {
            runTest {
                fetchData()
                actions.test {
                    assertEquals(ListViewModel.Action.ShowSnackBar(getString(R.string.error_no_internet)), awaitItem())
                }
            }
        }
    }

    @Test
    fun `WHEN initialization is completed THEN the corresponding todo list field follows the interactor's value`() {
        withSystemUnderTest {
            runTest {
                advanceUntilIdle()
                assertEquals(expected = TestData.testTodoList, todos.value)
            }
        }
    }

    @Test
    fun `WHEN todos list count changes THEN showEmptyListText property's value changes accordingly`() {
        withSystemUnderTest {
            runTest {
                showEmptyListText.test {
                    assertFalse(awaitItem())
                    fakeTodosDataSource.value = emptyList()
                    assertTrue(awaitItem())
                    fakeTodosDataSource.value = TestData.testTodoList
                    assertFalse(awaitItem())
                }
            }
        }
    }
}