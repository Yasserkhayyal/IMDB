package com.morse.movie.data.entity.movieresponse

import androidx.room.Entity
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class MovieResponse(
    val page: Int ?=0,
    val results: List<Result>?= arrayListOf(),
    val total_pages: Int?=0,
    val total_results: Int?=0
)

class MovieResponseDeserializer : ResponseDeserializable<MovieResponse> {

    override fun deserialize(content: String) =
        Gson().fromJson<MovieResponse>(content, MovieResponse::class.java)
}