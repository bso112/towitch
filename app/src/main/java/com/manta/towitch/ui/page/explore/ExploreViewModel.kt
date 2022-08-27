package com.manta.towitch.ui.page.explore

import androidx.lifecycle.ViewModel
import com.manta.towitch.data.MainRepository
import com.manta.towitch.data.entity.Game
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.utils.Logger
import com.manta.towitch.utils.onFailure
import com.manta.towitch.utils.onSuccess
import com.manta.towitch.utils.stateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    val games: StateFlow<List<Game>> = stateFlow(emptyList()) {
        mainRepository.fetchTopGames().onSuccess {
            emit(it.data)
        }.onFailure {
            Logger.d(it)
        }
    }


    val streams: StateFlow<List<Stream>> = stateFlow(initialValue = emptyList()) {
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

}