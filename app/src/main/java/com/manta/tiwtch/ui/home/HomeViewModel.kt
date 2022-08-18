package com.manta.tiwtch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.tiwtch.data.MainRepository
import com.manta.tiwtch.data.entity.Stream
import com.manta.tiwtch.data.entity.User
import com.manta.tiwtch.utils.mockUser
import com.manta.tiwtch.utils.onSuccess
import com.manta.tiwtch.utils.stateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val user: StateFlow<User> = stateFlow(
        initialValue = mockUser
    ) {
        mainRepository.fetchUsers().onSuccess {
            if (it.data.isNotEmpty()) {
                emit(it.data.first())
            }
        }
    }

    val followedStream: StateFlow<List<Stream>> = stateFlow(initialValue = emptyList()) {
        user.collect { user ->
            if(user == mockUser) return@collect
            mainRepository.fetchFollowedStreams(user.id).onSuccess { stream ->
                emit(stream.data)
            }
        }
    }

    val recommendedStream: StateFlow<List<Stream>> = stateFlow(initialValue = emptyList()) {
        mainRepository.fetchStreams().onSuccess {
            emit(it.data)
        }
    }

     val followings: StateFlow<List<User>> = stateFlow(initialValue = emptyList()) {
        user.collect { user ->
            if(user == mockUser) return@collect
            mainRepository.fetchFollowings(user.id).onSuccess { followedList ->
                emit(followedList.data.map { User(it.id, it.name, "") })
            }
        }
    }

    val offlineFollowings: StateFlow<List<User>> =
        followedStream.combine(followings) { streamList, followings ->
            val onlineFollowings = streamList.map { it.id }
            followings.filter { following -> !onlineFollowings.contains(following.id) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
}