package com.manta.tiwtch.data

import com.manta.tiwtch.common.PreferenceHelper
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val twitchApiService: TwitchApiService,
    private val twitchIdService: TwitchIdService,
    private val preferenceHelper: PreferenceHelper
) {
    suspend fun fetchAppToken() = twitchIdService.fetchAppToken()
    suspend fun fetchStreams() = twitchApiService.fetchStreams(preferenceHelper.twitchAppToken)
    suspend fun fetchFollowedStreams() = twitchApiService.fetchFollowedStreams(preferenceHelper.twitchUserToken)
}