package com.morse.movie.data.entity.movievideosresponse


import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.morse.movie.data.entity.moviereviewresponse.MovieReview

data class MovieVideoResponse(
    @SerializedName("id")
    var id: Int? = null, // 529203
    @SerializedName("results")
    var results: List<Result>? = null
)


class MovieVideoResponseDeserializer : ResponseDeserializable<MovieVideoResponse> {

    override fun deserialize(content: String) =
        Gson().fromJson<MovieVideoResponse>(content, MovieVideoResponse::class.java)
}