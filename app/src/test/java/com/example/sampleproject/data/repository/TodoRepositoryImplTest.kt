package com.example.sampleproject.data.repository

import com.example.sampleproject.TestData
import com.example.sampleproject.data.local.dao.TodoDao
import com.example.sampleproject.data.remote.api.TodoApi
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

class TodoRepositoryImplTest {
    private val mockTodoDao: TodoDao = mockk()
    private val mockTodoApi: TodoApi = mockk()

    private inline fun withSystemUnderTest(
        action: TodoRepositoryImpl.() -> Unit,
    ) {
        TodoRepositoryImpl(
            todoDao = mockTodoDao,
            todoApi = mockTodoApi
        ).apply(action)
    }

    @Before
    fun warmUp() {
        coEvery { mockTodoDao.getTodos() } returns flowOf(TestData.testTodoList)
        coEvery { mockTodoDao.getTodoById(any()) } returns TestData.testTodo
        coEvery { mockTodoDao.insertTodos(any()) } just Runs
        coEvery { mockTodoApi.fetchTodos() } returns TestData.testTodoDtoList
    }

    @Test
    fun `WHEN the dao has Todo items THEN todos reflects its value`() {
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
    fun `WHEN fetchTodos is called THEN the correct data is returned from the API`() {
        withSystemUnderTest {
            runTest {
                val response = fetchTodos()
                assertEquals(expected = TestData.testTodoDtoList, response)
            }
        }
    }

    @Test
    fun `WHEN saveTodos is called THEN DAO's correct method is called`() {
        val todos = TestData.testTodoList
        withSystemUnderTest {
            runTest {
                saveTodos(todos)
                coVerify (exactly = 1) { mockTodoDao.insertTodos(todos) }
            }
        }
    }

    @Test
    fun `WHEN getTodoById is called THEN DAO's correct method is called AND the correct Todo item is returned`() {
        val id = TestData.testTodo.id
        withSystemUnderTest {
            runTest {
                val todoFromDb = getTodoById(id)
                coVerify (exactly = 1) { mockTodoDao.getTodoById(id) }
                assertEquals(expected = TestData.testTodo, actual = todoFromDb)
            }
        }
    }
}