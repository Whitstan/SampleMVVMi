package com.example.sampleproject.ui.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleproject.R
import com.example.sampleproject.domain.interactor.TodoInteractor
import com.example.sampleproject.ui.common.actionDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class ListViewModelImpl @Inject constructor(
    private val context: Application,
    private val todoInteractor: TodoInteractor
) : ListViewModel, ViewModel() {

    override val actions = actionDispatcher<ListViewModel.Action>()

    override val todos = todoInteractor.todos.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    override val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val showEmptyListText = todos
        .map { it.isEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    override fun fetchData() {
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                actions.send(
                    ListViewModel.Action.ShowSnackBar(
                        when (throwable) {
                            is UnknownHostException -> context.getString(R.string.error_no_internet)
                            else -> throwable.message ?: context.getString(R.string.unknown_error)
                        }
                    )
                )
            }
            isLoading.value = false
        }
        viewModelScope.launch (errorHandler) {
            isLoading.value = true
            todoInteractor.fetchAndSaveTodos()
            isLoading.value = false
        }
    }
}