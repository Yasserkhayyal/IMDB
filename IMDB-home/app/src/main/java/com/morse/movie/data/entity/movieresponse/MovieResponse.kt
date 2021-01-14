package com.morse.movie.data.entity.movieresponse

import androidx.room.Entity
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page")
    val page: Int ?=0,
    @SerializedName("results")
    val results: List<Result>?= arrayListOf(),
    @SerializedName("total_pages")
    val total_pages: Int?=0,
    @SerializedName("total_results")
    val total_results: Int?=0
)

class MovieResponseDeserializer : ResponseDeserializable<MovieResponse> {

    override fun deserialize(content: String) =
        Gson().fromJson<MovieResponse>(content, MovieResponse::class.java)
}