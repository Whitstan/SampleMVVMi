package com.example.sampleproject.data.local.dao

import com.example.sampleproject.TestData
import com.example.sampleproject.data.local.database.TodoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class TodoDaoTest : DaoTest<TodoDao>(){
    override fun getDao(db: TodoDatabase) = db.getTodoDao()

    @Test
    fun WHEN_getTodos_function_is_called_THEN_the_correct_data_is_returned() {
        withDao {
            runTest {
                db.getTodoDao().insertTodos(TestData.testTodoList)
                val todosFlow = getTodos()
                assertEquals(expected = todosFlow.first(), actual = TestData.testTodoList)
            }
        }
    }

    @Test
    fun WHEN_insertTodos_function_is_called_THEN_the_correct_data_is_inserted() {
        val todoList = TestData.testTodoList
        withDao {
            runTest {
                insertTodos(todoList)
                assertEquals(expected = todoList, actual = getTodos().first())
            }
        }
    }

    @Test
    fun WHEN_getTodoById_function_is_called_THEN_the_correct_todo_item_is_returned() {
        val id = TestData.testTodo.id
        withDao {
            runTest {
                val todoFromDb = getTodoById(id)
                assertEquals(expected = getTodos().first().find { it.id == id }, actual = todoFromDb)
            }
        }
    }
}