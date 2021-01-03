package com.morse.movie.data.entity.movievideosresponse


import com.google.gson.annotations.SerializedName

data class MovieVideoResponse(
    @SerializedName("id")
    var id: Int? = null, // 529203
    @SerializedName("results")
    var results: List<Result>? = null
)