package com.example.sampleproject.ui.list

import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.ui.common.ActionSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ListViewModel : ActionSource<ListViewModel.Action>{
    val todos: StateFlow<List<Todo>>

    val isLoading : MutableStateFlow<Boolean>
    val showEmptyListText : StateFlow<Boolean>

    fun fetchData()

    sealed class Action {
        data class ShowSnackBar(val message: String) : Action()
    }
}