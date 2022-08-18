package com.manta.tiwtch.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.manta.tiwtch.common.Consts
import com.manta.tiwtch.common.PreferenceHelper
import com.manta.tiwtch.data.MainRepository
import com.manta.tiwtch.data.entity.StreamData
import com.manta.tiwtch.utils.stateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    val streams: StateFlow<List<StreamData>> = stateFlow(initialValue =  emptyList()) {
        authenticate()
        val response = mainRepository.fetchFollowedStreams()
        if (response.isSuccessful) {
            response.body()?.let { emit(it.data) }
        }
    }

    suspend fun authenticate() {
        if(preferenceHelper.getTwitchAccessToken() != null){
            return
        }
        val response = mainRepository.authenticate()
        if (response.isSuccessful) {
            response.body()?.let { token ->
                preferenceHelper.setTwitchAccessToken(token.token)
            }
        }

    }
}