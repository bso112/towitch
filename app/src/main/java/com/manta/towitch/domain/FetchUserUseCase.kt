package com.manta.towitch.domain

import com.manta.towitch.data.MainRepository
import com.manta.towitch.data.entity.User
import com.manta.towitch.utils.ExceptionHandler
import com.manta.towitch.utils.defaultExceptionHandler
import com.manta.towitch.utils.onFailure
import com.manta.towitch.utils.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchUserUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val ioDispatcher: CoroutineDispatcher

) {

    operator fun invoke(onFailure: ExceptionHandler = defaultExceptionHandler): Flow<User> = flow {
        mainRepository.fetchUsers().onSuccess {
            if (it.data.isNotEmpty()) {
                emit(it.data.first())
            }
        }.onFailure(onFailure)
    }.flowOn(ioDispatcher)
}