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

class FetchFollowingsUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val ioDispatcher: CoroutineDispatcher

) {

    operator fun invoke(userId : String, onFailure : ExceptionHandler = defaultExceptionHandler): Flow<List<User>> = flow {
        mainRepository.fetchFollowings(userId).onSuccess { followedList ->
            val idList = followedList.data.map { it.id }
            mainRepository.fetchUsers(idList).onSuccess { results ->
                emit(results.data)
            }
        }.onFailure(onFailure)
    }.flowOn(ioDispatcher)
}