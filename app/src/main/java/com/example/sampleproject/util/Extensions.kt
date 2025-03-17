package com.example.sampleproject.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.data.remote.TodoDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun List<TodoDto>.toLocalTodos() : List<Todo>{
    return this.map { todoDto ->
        Todo(
            id = todoDto.id,
            userId = todoDto.userId,
            title = todoDto.title,
            isCompleted = todoDto.isCompleted
        )
    }
}

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}
