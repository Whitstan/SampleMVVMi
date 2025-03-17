package com.example.sampleproject.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface ActionSource<T> {
    val actions: Flow<T>
}

class ActionDispatcher<T> : AbstractFlow<T>() {
    private val channel = Channel<T>()
    private val actions = channel.receiveAsFlow()

    fun send(coroutineScope: CoroutineScope, action: T) {
        coroutineScope.launch {
            channel.send(action)
        }
    }

    override suspend fun collectSafely(collector: FlowCollector<T>) {
        collector.emitAll(actions)
    }
}

class ScopedActionDispatcher<T>(
    private val coroutineScope: CoroutineScope,
    private val delegate: ActionDispatcher<T> = ActionDispatcher(),
) : Flow<T> by delegate {

    fun send(action: T) {
        delegate.send(coroutineScope, action)
    }
}

fun <T> ViewModel.actionDispatcher() = ScopedActionDispatcher<T>(viewModelScope)