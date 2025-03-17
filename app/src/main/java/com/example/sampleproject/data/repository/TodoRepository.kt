package com.example.sampleproject.data.repository

import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.data.remote.TodoDto
import io.reactivex.rxjava3.core.Maybe
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    val todos: Flow<List<Todo>>
    suspend fun fetchTodos() : List<TodoDto>
    suspend fun saveTodos(todos: List<Todo>)
    suspend fun getTodoById(id: Int): Todo?
}