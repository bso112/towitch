package com.manta.towitch.data.entity

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("tag_id")
    val tagId : String,
    @SerializedName("localization_names")
    val names : Map<String, String>
)
