package com.example.sampleproject.domain

import com.example.sampleproject.TestData
import com.example.sampleproject.data.repository.TodoRepository
import com.example.sampleproject.domain.interactor.TodoInteractorImpl
import com.example.sampleproject.util.toLocalTodos
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TodoInteractorImplTest {
    private val mockTodoRepository: TodoRepository = mockk()

    private inline fun withSystemUnderTest(
        action: TodoInteractorImpl.() -> Unit,
    ) {
        TodoInteractorImpl(mockTodoRepository).apply(action)
    }

    @Before
    fun warmUp() {
        coEvery { mockTodoRepository.todos } returns flowOf(TestData.testTodoList)
        coEvery { mockTodoRepository.getTodoById(any()) } returns TestData.testTodo
        coEvery { mockTodoRepository.saveTodos(any()) } just Runs
        coEvery { mockTodoRepository.fetchTodos() } returns TestData.testTodoDtoList
    }

    @Test
    fun `WHEN todos change in the repository THEN the interactor will returns those`() {
        withSystemUnderTest {
            runTest {
                assertEquals(
                    expected = flowOf(TestData.testTodoList).toList(),
                    actual = todos.toList()
                )
            }
        }
    }

    @Test
    fun `WHEN fetchAndSaveTodos is called THEN the corresponding methods of the repository are called`() {
        withSystemUnderTest {
            runTest {
                fetchAndSaveTodos()
                coVerify (exactly = 1) { mockTodoRepository.fetchTodos() }
                coVerify (exactly = 1) { mockTodoRepository.saveTodos(TestData.testTodoDtoList.toLocalTodos()) }
            }
        }
    }

    @Test
    fun `WHEN getTodoById is called THEN the corresponding method of the repository is called AND the correct Todo item is returned`() {
        val id = TestData.testTodo.id
        withSystemUnderTest {
            runTest {
                val todoFromDb = getTodoById(id)
                coVerify (exactly = 1) { mockTodoRepository.getTodoById(id) }
                assertEquals(expected = TestData.testTodo, actual = todoFromDb)
            }
        }
    }
}