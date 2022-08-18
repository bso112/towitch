package com.manta.tiwtch.data

import com.manta.tiwtch.BuildConfig
import com.manta.tiwtch.data.entity.Stream
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitchApiService {

    @GET("/helix/streams")
    suspend fun fetchStreams(
        @Header("Authorization") appToken: String
    ): Response<Stream>

    @GET("/helix/streams/followed")
    suspend fun fetchFollowedStreams(
        @Header("Authorization") userToken: String,
        @Query("user_id") userId: String = "595994940"
    ): Response<Stream>

}