package com.manta.towitch.ui.page.finding

import androidx.lifecycle.ViewModel
import com.manta.towitch.data.MainRepository
import com.manta.towitch.data.entity.Clip
import com.manta.towitch.data.entity.Game
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.utils.Logger
import com.manta.towitch.utils.onFailure
import com.manta.towitch.utils.onSuccess
import com.manta.towitch.utils.stateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FindingViewModel @Inject constructor(
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

    val recommendedStreams = streams.map { it.shuffled().take(10) }

    val smallStreams = streams.map { it.reversed().take(10) }

    val justcChattingStreams =
        streams.map { it.asSequence().filter { it.gameName == "Just Chatting" }.take(10).toList() }

    val clips: StateFlow<List<Clip>> = stateFlow(emptyList()) {
        mainRepository.fetchClips("49045679").onSuccess { data ->
            val clips = data.data
            mainRepository.fetchUsers(clips.map { it.broadCasterId })
                .onSuccess { users ->
                    mainRepository.fetchGames(clips.map { it.gameId })
                        .onSuccess { games ->
                            clips.map { clip ->
                                clip.copy(
                                    broadCasterProfileImageUrl = users.data.first().profileImageUrl,
                                    gameName = games.data.find { it.id == clip.gameId }?.name
                                )
                            }.also {
                                emit(it)
                            }
                        }
                }
        }.onFailure {
            Logger.d(it)
        }
    }
}