package com.morse.movie.data.entity.moviereviewresponse


import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.morse.movie.data.entity.movieresponse.MovieResponse

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

class MovieReviewResponseDeserializer : ResponseDeserializable<MovieReview> {

    override fun deserialize(content: String) =
        Gson().fromJson<MovieReview>(content, MovieReview::class.java)
}