package com.manta.towitch.data.entity

import com.google.gson.annotations.SerializedName

//{
//    "data": [
//    {
//        "id": "41375541868",
//        "user_id": "459331509",
//        "user_login": "auronplay",
//        "user_name": "auronplay",
//        "game_id": "494131",
//        "game_name": "Little Nightmares",
//        "type": "live",
//        "title": "hablamos y le damos a Little Nightmares 1",
//        "viewer_count": 78365,
//        "started_at": "2021-03-10T15:04:21Z",
//        "language": "es",
//        "thumbnail_url": "https://static-cdn.jtvnw.net/previews-ttv/live_user_auronplay-{width}x{height}.jpg",
//        "tag_ids": [
//        "d4bb9c58-2141-4881-bcdc-3fe0505457d1"
//        ],
//        "is_mature": false
//    },
//    ...
//    ],
//    "pagination": {
//    "cursor": "eyJiIjp7IkN1cnNvciI6ImV5SnpJam8zT0RNMk5TNDBORFF4TlRjMU1UY3hOU3dpWkNJNlptRnNjMlVzSW5RaU9uUnlkV1Y5In0sImEiOnsiQ3Vyc29yIjoiZXlKeklqb3hOVGs0TkM0MU56RXhNekExTVRZNU1ESXNJbVFpT21aaGJITmxMQ0owSWpwMGNuVmxmUT09In19"
//}
//}

data class Stream(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("viewer_count")
    val viewerCount: Int,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("game_name")
    val gameName: String,
    val tags: List<String>?,
    val userProfileImageUrl: String?
) {
    fun getSizedThumbnailUrl(width: Int, height: Int): String {
        return thumbnailUrl.replace("{width}", width.toString())
            .replace("{height}", width.toString())
    }
}
