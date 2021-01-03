package com.morse.movie.data.entity.movievideosresponse


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("id")
    var id: String? = null, // 5f6aee86a6e2d2003a64b43c
    @SerializedName("iso_3166_1")
    var iso31661: String? = null, // US
    @SerializedName("iso_639_1")
    var iso6391: String? = null, // en
    @SerializedName("key")
    var key: String? = null, // GkXeVIfbJOw
    @SerializedName("name")
    var name: String? = null, // THE CROODS: A NEW AGE | Official Trailer
    @SerializedName("site")
    var site: String? = null, // YouTube
    @SerializedName("size")
    var size: Int? = null, // 1080
    @SerializedName("type")
    var type: String? = null // Trailer
)