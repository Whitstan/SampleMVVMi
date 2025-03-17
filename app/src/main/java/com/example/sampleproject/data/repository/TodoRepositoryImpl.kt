package com.example.sampleproject.data.repository

import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.data.local.dao.TodoDao
import com.example.sampleproject.data.remote.api.TodoApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) : TodoRepository {
    override val todos: Flow<List<Todo>> = todoDao.getTodos()
    override suspend fun fetchTodos() = todoApi.fetchTodos()
    override suspend fun getTodoById(id: Int) = todoDao.getTodoById(id)
    override suspend fun saveTodos(todos: List<Todo>) = todoDao.insertTodos(todos)
}
