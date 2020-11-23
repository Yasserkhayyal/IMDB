package com.morse.movie.remote.entity.movieresponse

data class MovieResponse(
    val page: Int ?=0,
    val results: List<Result>?= arrayListOf(),
    val total_pages: Int?=0,
    val total_results: Int?=0
)