package com.manta.tiwtch.data

import com.manta.tiwtch.data.entity.Stream
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface TwitchApiService {

    @GET("/helix/streams")
    suspend fun fetchStreams(): Response<Stream>

}