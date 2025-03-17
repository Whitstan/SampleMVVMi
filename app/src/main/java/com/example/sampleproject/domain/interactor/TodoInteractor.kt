package com.example.sampleproject.domain.interactor

import com.example.sampleproject.data.local.Todo
import kotlinx.coroutines.flow.Flow

interface TodoInteractor {
    val todos: Flow<List<Todo>>
    suspend fun fetchAndSaveTodos()
    suspend fun getTodoById(id: Int) : Todo?
}