package com.manta.tiwtch.ui.home

import androidx.lifecycle.ViewModel
import com.manta.tiwtch.common.PreferenceHelper
import com.manta.tiwtch.data.MainRepository
import com.manta.tiwtch.data.entity.StreamData
import com.manta.tiwtch.utils.stateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val followedStream: StateFlow<List<StreamData>> = stateFlow(initialValue = emptyList()) {
        val response = mainRepository.fetchFollowedStreams()
        if (response.isSuccessful) {
            response.body()?.let { emit(it.data) }
        }
    }

    val recommendedStream : StateFlow<List<StreamData>> = stateFlow(initialValue = emptyList()) {
        val response = mainRepository.fetchStreams()
        if (response.isSuccessful) {
            response.body()?.let { emit(it.data) }
        }
    }
}