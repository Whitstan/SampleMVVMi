package com.example.sampleproject.data.remote.api

import com.example.sampleproject.data.remote.TodoDto
import retrofit2.http.GET

interface TodoApi {
    @GET("todos/")
    suspend fun fetchTodos(): List<TodoDto>
}