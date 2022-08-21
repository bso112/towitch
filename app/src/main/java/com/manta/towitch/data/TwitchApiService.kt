package com.manta.towitch.data

import com.manta.towitch.data.entity.BaseEntity
import com.manta.towitch.data.entity.Following
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.data.entity.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TwitchApiService {

    @GET("/helix/streams")
    suspend fun fetchStreams(
        @Header("Authorization") appToken: String
    ): Response<BaseEntity<Stream>>

    @GET("/helix/streams/followed")
    suspend fun fetchFollowedStreams(
        @Header("Authorization") userToken: String,
        @Query("user_id") userId: String
    ): Response<BaseEntity<Stream>>

    @GET("/helix/users")
    suspend fun fetchUsers(
        @Header("Authorization") userToken: String,
    ): Response<BaseEntity<User>>

    @GET("/helix/users/follows")
    suspend fun fetchFollowings(
        @Header("Authorization") userToken: String,
        @Query("from_id") userId: String
    ): Response<BaseEntity<Following>>

}