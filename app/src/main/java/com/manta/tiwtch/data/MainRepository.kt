package com.manta.tiwtch.data

import javax.inject.Inject

class MainRepository @Inject constructor(
    private val twitchApiService: TwitchApiService,
    private val twitchIdService: TwitchIdService
) {
    suspend fun authenticate() = twitchIdService.authenticate()
    suspend fun fetchStreams() = twitchApiService.fetchStreams()
    suspend fun fetchFollowedStreams() = twitchApiService.fetchFollowedStreams()
}