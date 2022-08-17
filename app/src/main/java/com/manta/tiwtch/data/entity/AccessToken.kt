package com.manta.tiwtch.data.entity

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token")
    val token: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("token_type")
    val type: String
)