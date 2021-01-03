package com.morse.movie.data.entity.moviereviewresponse


import com.google.gson.annotations.SerializedName

data class MovieReview(
    @SerializedName("id")
    var id: Int? = null, // 529203
    @SerializedName("page")
    var page: Int? = null, // 1
    @SerializedName("results")
    var results: List<Result>? = null,
    @SerializedName("total_pages")
    var totalPages: Int? = null, // 1
    @SerializedName("total_results")
    var totalResults: Int? = null // 1
)