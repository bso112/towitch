package com.manta.towitch.data

import com.manta.towitch.data.entity.BaseEntity
import com.manta.towitch.data.entity.Category
import com.manta.towitch.data.entity.Clip
import com.manta.towitch.data.entity.Following
import com.manta.towitch.data.entity.Game
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.data.entity.Tag
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
        @Query("id") userId: List<String> = emptyList()
    ): Response<BaseEntity<User>>

    @GET("/helix/users/follows")
    suspend fun fetchFollowings(
        @Header("Authorization") userToken: String,
        @Query("from_id") userId: String
    ): Response<BaseEntity<Following>>

    @GET("/helix/streams/tags")
    suspend fun fetchTags(
        @Header("Authorization") appToken: String,
        @Query("broadcaster_id") broadcasterId : String
    ) : Response<BaseEntity<Tag>>

    @GET("/helix/search/categories")
    suspend fun fetchCategories(
        @Header("Authorization") appToken: String,
        @Query("query") query : String
    ) :Response<BaseEntity<Category>>

    @GET("/helix/clips")
    suspend fun fetchClips(
        @Header("Authorization") appToken: String,
        @Query("game_id") gameId : String = "",
        @Query("broadcaster_id") broadcasterId: String = "",
    ) : Response<BaseEntity<Clip>>

    @GET("/helix/games")
    suspend fun fetchGames(
        @Header("Authorization") appToken: String,
        @Query("id") gameId: List<String>
    ) : Response<BaseEntity<Game>>

    @GET("/helix/games/top")
    suspend fun fetchTopGames(
        @Header("Authorization") appToken: String
    ) : Response<BaseEntity<Game>>
}