package com.manta.towitch.ui.home

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.towitch.data.MainRepository
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.data.entity.User
import com.manta.towitch.utils.mockUser
import com.manta.towitch.utils.onSuccess
import com.manta.towitch.utils.stateFlow
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
            if (user == mockUser) return@collect
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
            if (user == mockUser) return@collect
            mainRepository.fetchFollowings(user.id).onSuccess { followedList ->
                val idList = followedList.data.map { it.id }
                mainRepository.fetchUsers(idList).onSuccess { results ->
                    emit(results.data)
                }
            }
        }
    }

    val offlineFollowings: StateFlow<List<User>> =
        followedStream.combine(followings) { streamList, followings ->
            val onlineFollowings = streamList.map { it.userName }
            followings.filter { following -> !onlineFollowings.contains(following.name) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
}