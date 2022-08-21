package com.manta.towitch.data

import com.manta.towitch.common.PreferenceHelper
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val twitchApiService: TwitchApiService,
    private val twitchIdService: TwitchIdService,
    private val preferenceHelper: PreferenceHelper
) {
    suspend fun fetchAppToken() = twitchIdService.fetchAppToken()
    suspend fun fetchStreams() = twitchApiService.fetchStreams(preferenceHelper.twitchAppToken)
    suspend fun fetchFollowedStreams(userId: String) =
        twitchApiService.fetchFollowedStreams(preferenceHelper.twitchUserToken, userId)

    /**
     * @param userId if [userId] is not specified, the user is looked up by Bearer token.
     */
    suspend fun fetchUsers(userId : List<String> = emptyList()) = twitchApiService.fetchUsers(userToken = preferenceHelper.twitchUserToken, userId = userId)
    suspend fun fetchFollowings(userId: String) =
        twitchApiService.fetchFollowings(preferenceHelper.twitchUserToken, userId)

    suspend fun fetchTag(broadcasterId : String) = twitchApiService.fetchTags(appToken = preferenceHelper.twitchAppToken, broadcasterId = broadcasterId)
}