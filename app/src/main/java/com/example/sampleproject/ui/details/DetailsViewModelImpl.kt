package com.example.sampleproject.ui.details

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleproject.R
import com.example.sampleproject.domain.interactor.TodoInteractor
import com.example.sampleproject.ui.common.actionDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModelImpl @Inject constructor(
    private val context: Application,
    private val todoInteractor: TodoInteractor
): DetailsViewModel, ViewModel() {

    override val actions = actionDispatcher<DetailsViewModel.Action>()

    override val title: MutableStateFlow<String?> = MutableStateFlow(null)
    override val id: MutableStateFlow<String?> = MutableStateFlow(null)
    override val isCompleted: MutableStateFlow<String?> = MutableStateFlow(null)
    override val userId: MutableStateFlow<String?> = MutableStateFlow(null)
    override val backgroundColor: MutableStateFlow<Int?> = MutableStateFlow(null)

    override fun setArguments(todoId: Int) {
        viewModelScope.launch {
            val todo = todoInteractor.getTodoById(todoId) ?: return@launch
            val completed = todo.isCompleted
            title.value = todo.title
            id.value = String.format(context.getString(R.string.text_details_id), todoId)
            isCompleted.value = context.getString(
                if (completed) R.string.text_details_completed else R.string.text_details_not_completed
            )
            userId.value = String.format(context.getString(R.string.text_details_user_id), todo.userId)
            backgroundColor.value = ContextCompat.getColor(
                context,
                if (completed) R.color.completedBackground else R.color.notCompletedBackground
            )
        }
    }

    override fun onBackButtonClicked() {
        viewModelScope.launch {
            actions.send(DetailsViewModel.Action.NavigateBack)
        }
    }
}