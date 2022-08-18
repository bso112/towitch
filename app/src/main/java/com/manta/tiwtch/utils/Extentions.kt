package com.manta.tiwtch.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*


fun String?.toSafe() = this ?: ""

fun <T> ViewModel.stateFlow(
    initialValue: T,
    block: suspend FlowCollector<T>.() -> Unit
): StateFlow<T> =
    flow(block).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), initialValue)
