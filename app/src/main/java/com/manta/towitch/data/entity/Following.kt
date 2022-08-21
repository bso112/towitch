package com.manta.towitch.data.entity

import com.google.gson.annotations.SerializedName

data class Following(
    @SerializedName("to_id")
    val id : String,
    @SerializedName("to_name")
    val name : String
)
