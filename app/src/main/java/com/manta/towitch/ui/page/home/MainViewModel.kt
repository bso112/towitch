package com.manta.towitch.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.towitch.data.MainRepository
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.data.entity.User
import com.manta.towitch.utils.mockUser
import com.manta.towitch.utils.onSuccess
import com.manta.towitch.utils.stateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
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
                val idList = stream.data.map { it.userId }
                mainRepository.fetchUsers(idList).onSuccess { results ->
                    stream.data.sortedBy { it.userId }
                    results.data.sortedBy { it.id }
                    emit(stream.data.zip(results.data)
                        .map { it.first.copy(userProfileImageUrl = it.second.profileImageUrl) })
                }
            }
        }
    }

    val recommendedStream: StateFlow<List<Stream>> = stateFlow(initialValue = emptyList()) {
        mainRepository.fetchStreams().onSuccess { stream ->
            val idList = stream.data.map { it.userId }
            mainRepository.fetchUsers(idList).onSuccess { results ->
                stream.data.sortedBy { it.userId }
                results.data.sortedBy { it.id }
                emit(stream.data.zip(results.data)
                    .map { it.first.copy(userProfileImageUrl = it.second.profileImageUrl) })
            }
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