package com.manta.towitch.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


fun String?.toSafe() = this ?: ""

fun <T> Flow<T>.toStateFlow(viewModel: ViewModel, initialValue: T): StateFlow<T> =
    stateIn(viewModel.viewModelScope, SharingStarted.WhileSubscribed(5000L), initialValue)

suspend fun <T> Response<T>.onSuccess(callback: suspend (T) -> Unit): Response<T> {
    if (isSuccessful) {
        body()?.also {
            callback(it)
        }
    }
    return this
}

suspend fun <T> Response<T>.onFailure(callback: suspend (String) -> Unit): Response<T> {
    if (!isSuccessful) {
        callback("[${code()}] ${message()}")
    }
    return this
}

fun Int.minuteToTimeString(): String {
    val minutes = (this % 3600) / 60;
    val seconds = this % 60;

    return String.format("%02d:%02d", minutes, seconds);
}

fun String.toDateString(): String {
    val regex = Regex("(.*)T(.*)Z")
    val match = regex.find(this) ?: return this
    val dateString = match.groupValues[1]
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString) ?: this
    return SimpleDateFormat("MM월 dd일", Locale.KOREA).format(date)
}