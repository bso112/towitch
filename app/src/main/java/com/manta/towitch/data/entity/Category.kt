package com.manta.towitch.data.entity

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("name")
    val name: String,
    @SerializedName("box_art_url")
    val imageUrl: String
)