package com.manta.towitch.domain

import com.manta.towitch.data.MainRepository
import com.manta.towitch.data.entity.Game
import com.manta.towitch.utils.Logger
import com.manta.towitch.utils.onFailure
import com.manta.towitch.utils.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchTopGamesUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    operator fun invoke(): Flow<List<Game>> = flow {
        mainRepository.fetchTopGames().onSuccess {
            emit(it.data)
        }.onFailure {
            Logger.d(it)
        }
    }.flowOn(ioDispatcher)
}