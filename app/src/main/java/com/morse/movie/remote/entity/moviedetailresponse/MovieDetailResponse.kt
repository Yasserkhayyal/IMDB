package com.morse.movie.remote.entity.moviedetailresponse

data class MovieDetailResponse(
    val adult: Boolean ?=false,
    val backdrop_path: String ?= "",
    val belongs_to_collection: Any ?= Any(),
    val budget: Int?= 0,
    val genres: List<Genre> = arrayListOf(),
    val homepage: String?= "",
    val id: Int?= 0,
    val imdb_id: String?= "",
    val original_language: String?= "",
    val original_title: String?= "",
    val overview: String?= "",
    val popularity: Double?= 0.0,
    val poster_path: String?= "",
    val production_companies: List<ProductionCompany> ?= arrayListOf(),
    val production_countries: List<ProductionCountry> ?= arrayListOf(),
    val release_date: String?= "",
    val revenue: Int?= 0,
    val runtime: Int?= 0,
    val spoken_languages: List<SpokenLanguage> ?= arrayListOf(),
    val status: String?= "",
    val tagline: String?= "",
    val title: String?= "",
    val video: Boolean ?=false,
    val vote_average: Double ?= 0.0,
    val vote_count: Int ?= 0
)