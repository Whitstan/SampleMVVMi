package com.example.sampleproject.domain.interactor

import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.data.repository.TodoRepository
import com.example.sampleproject.util.toLocalTodos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoInteractorImpl @Inject constructor(private val todoRepository: TodoRepository) : TodoInteractor {
    override val todos: Flow<List<Todo>> = todoRepository.todos

    override suspend fun fetchAndSaveTodos() {
        val todoDtoList = todoRepository.fetchTodos()
        todoRepository.saveTodos(todoDtoList.toLocalTodos())
    }

    override suspend fun getTodoById(id: Int) = todoRepository.getTodoById(id)
}