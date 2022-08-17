package com.manta.tiwtch.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.tiwtch.common.Consts
import com.manta.tiwtch.data.MainRepository
import com.manta.tiwtch.data.entity.StreamData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    val streams: StateFlow<List<StreamData>> = flow {
        authenticate()
        val response = mainRepository.fetchStreams()
        if (response.isSuccessful) {
            response.body()?.let { emit(it.data) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    suspend fun authenticate() {
        val response = mainRepository.authenticate()
        if (response.isSuccessful) {
            response.body()?.let { token ->
                preferences.edit().putString(Consts.TWITCH_ACCESS_TOKEN, token.token)
                    .apply()
            }
        }

    }
}