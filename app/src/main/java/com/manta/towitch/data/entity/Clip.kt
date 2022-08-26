package com.manta.towitch.data.entity

import com.google.gson.annotations.SerializedName

data class Clip(
    @SerializedName("url")
    val url: String,
    @SerializedName("broadcaster_name")
    val broadCasterName: String,
    @SerializedName("broadcaster_id")
    val broadCasterId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("view_count")
    val viewCount: Int,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("duration")
    val duration: Float,
    val broadCasterProfileImageUrl: String?,
    val gameName: String?
)