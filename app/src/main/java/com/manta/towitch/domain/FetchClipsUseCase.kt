package com.manta.towitch.domain

import com.manta.towitch.data.MainRepository
import com.manta.towitch.utils.ExceptionHandler
import com.manta.towitch.utils.defaultExceptionHandler
import com.manta.towitch.utils.onFailure
import com.manta.towitch.utils.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchClipsUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    operator fun invoke(onFailure : ExceptionHandler = defaultExceptionHandler) = flow {
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
        }.onFailure(onFailure)
    }.flowOn(ioDispatcher)
}