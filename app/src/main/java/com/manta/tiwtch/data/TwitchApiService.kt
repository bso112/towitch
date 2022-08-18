package com.manta.tiwtch.data

import com.manta.tiwtch.BuildConfig
import com.manta.tiwtch.data.entity.Stream
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitchApiService {

    @GET("/helix/streams")
    suspend fun fetchStreams(): Response<Stream>

    @GET("/helix/streams/followed")
    suspend fun fetchFollowedStreams(@Query("user_id") userId : String = "595994940"): Response<Stream>

}