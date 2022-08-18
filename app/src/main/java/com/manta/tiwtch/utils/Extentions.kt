package com.manta.tiwtch.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import retrofit2.Response


fun String?.toSafe() = this ?: ""

fun <T> ViewModel.stateFlow(
    initialValue: T,
    block: suspend FlowCollector<T>.() -> Unit
): StateFlow<T> =
    flow(block).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), initialValue)


suspend fun <T> Response<T>.onSuccess(callback: suspend (T) -> Unit) {
    if (isSuccessful) {
        body()?.also {
            callback(it)
        }
    }
}

suspend fun <T> Response<T>.onFailure(callback: suspend (String) -> Unit) {
    if (!isSuccessful) {
        callback("[${code()}] ${message()}")
    }
}

