package com.manta.towitch.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.data.entity.User
import com.manta.towitch.domain.FetchFollowedStreamUseCase
import com.manta.towitch.domain.FetchFollowingsUseCase
import com.manta.towitch.domain.FetchStreamUseCase
import com.manta.towitch.domain.FetchUserUseCase
import com.manta.towitch.utils.mockUser
import com.manta.towitch.utils.toStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    fetchUserUseCase: FetchUserUseCase,
    fetchStreamUseCase: FetchStreamUseCase,
    fetchFollowedStreamUseCase: FetchFollowedStreamUseCase,
    fetchFollowingsUseCase: FetchFollowingsUseCase
) : ViewModel() {

    val user: StateFlow<User> = fetchUserUseCase().toStateFlow(this, mockUser)

    val followedStream: StateFlow<List<Stream>> = user.flatMapLatest { user ->
        fetchFollowedStreamUseCase(user.id)
    }.toStateFlow(this, emptyList())

    val recommendedStream: StateFlow<List<Stream>> = fetchStreamUseCase().toStateFlow(this, emptyList())


    val followings: StateFlow<List<User>> = user.flatMapLatest { user ->
        if (user == mockUser) return@flatMapLatest emptyFlow()
        fetchFollowingsUseCase(user.id)
    }.toStateFlow(this, emptyList())

    val offlineFollowings: StateFlow<List<User>> =
        followedStream.combine(followings) { streamList, followings ->
            val onlineFollowings = streamList.map { it.userName }
            followings.filter { following -> !onlineFollowings.contains(following.name) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


}