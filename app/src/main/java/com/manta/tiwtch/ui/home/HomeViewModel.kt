package com.manta.tiwtch.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.manta.tiwtch.common.Consts
import com.manta.tiwtch.data.MainRepository
import com.manta.tiwtch.data.entity.StreamData
import com.manta.tiwtch.utils.stateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    val streams: StateFlow<List<StreamData>> = stateFlow(initialValue =  emptyList()) {
        authenticate()
        val response = mainRepository.fetchStreams()
        if (response.isSuccessful) {
            response.body()?.let { emit(it.data) }
        }
    }

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