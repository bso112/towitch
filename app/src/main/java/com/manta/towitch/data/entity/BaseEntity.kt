package com.manta.towitch.data.entity

import com.google.gson.annotations.SerializedName

data class BaseEntity<T>(
    @SerializedName("data")
    val data: List<T>
)