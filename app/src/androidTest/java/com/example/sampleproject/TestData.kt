package com.example.sampleproject

import com.example.sampleproject.data.local.Todo

object TestData {
    val testTodo: Todo = Todo(
        id = 1,
        userId = 1,
        isCompleted = true,
        title = "Title"
    )

    val testTodoList : List<Todo> = listOf(
        Todo(
            id = 1,
            userId = 1,
            isCompleted = true,
            title = "Title1"
        ),
        Todo(
            id = 2,
            userId = 1,
            isCompleted = false,
            title = "Title2"
        ),
        Todo(
            id = 3,
            userId = 2,
            isCompleted = true,
            title = "Title3"
        )
    )
}