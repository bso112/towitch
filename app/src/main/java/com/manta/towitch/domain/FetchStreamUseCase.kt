package com.manta.towitch.domain

import com.manta.towitch.data.MainRepository
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.utils.ExceptionHandler
import com.manta.towitch.utils.defaultExceptionHandler
import com.manta.towitch.utils.onFailure
import com.manta.towitch.utils.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchStreamUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    operator fun invoke(onFailure : ExceptionHandler = defaultExceptionHandler): Flow<List<Stream>> = flow {
        mainRepository.fetchStreams().onSuccess { stream ->
            val idList = stream.data.map { it.userId }
            mainRepository.fetchUsers(idList).onSuccess { results ->
                stream.data.sortedBy { it.userId }
                results.data.sortedBy { it.id }
                emit(stream.data.zip(results.data)
                    .map { it.first.copy(userProfileImageUrl = it.second.profileImageUrl) })
            }
        }.onFailure(onFailure)
    }.flowOn(ioDispatcher)
}