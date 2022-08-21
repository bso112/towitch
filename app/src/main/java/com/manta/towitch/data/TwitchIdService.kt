package com.manta.towitch.data

import com.manta.towitch.BuildConfig
import com.manta.towitch.data.entity.AccessToken
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TwitchIdService {

    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun fetchAppToken(
        @Field("client_id") clientId: String = BuildConfig.TWITCH_CLIENT_ID,
        @Field("client_secret") clientSecret: String = BuildConfig.TWITCH_SECRET,
        @Field("grant_type") grantType: String = "client_credentials"
    ): Response<AccessToken>
}