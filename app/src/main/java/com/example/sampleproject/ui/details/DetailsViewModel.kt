package com.example.sampleproject.ui.details

import com.example.sampleproject.ui.common.ActionSource
import kotlinx.coroutines.flow.MutableStateFlow

interface DetailsViewModel : ActionSource<DetailsViewModel.Action> {
    val title : MutableStateFlow<String?>
    val backgroundColor : MutableStateFlow<Int?>
    val id : MutableStateFlow<String?>
    val isCompleted : MutableStateFlow<String?>
    val userId : MutableStateFlow<String?>

    fun setArguments(todoId: Int)
    fun onBackButtonClicked()

    sealed class Action {
        data object NavigateBack : Action()
    }
}