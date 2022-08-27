package com.manta.towitch.data.entity

import com.google.gson.annotations.SerializedName

data class Game(
    @SerializedName("box_art_url")
    val imageUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
){
    fun getSizedThumbnailUrl(width: Int, height: Int): String {
        return imageUrl.replace("{width}", width.toString())
            .replace("{height}", width.toString())
    }
}