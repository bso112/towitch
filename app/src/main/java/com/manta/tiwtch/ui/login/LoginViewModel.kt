package com.manta.tiwtch.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.tiwtch.common.PreferenceHelper
import com.manta.tiwtch.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val preferenceHelper: PreferenceHelper
) : ViewModel() {

    fun fetchTwitchAppToken() {
        viewModelScope.launch {
            val response = mainRepository.fetchAppToken()
            if (response.isSuccessful) {
                response.body()?.also {
                    preferenceHelper.twitchAppToken = it.token
                }
            }
        }
    }
}