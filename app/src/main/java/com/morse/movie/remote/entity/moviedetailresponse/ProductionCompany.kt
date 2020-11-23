package com.morse.movie.remote.entity.moviedetailresponse

data class ProductionCompany(
    val id: Int ?= 0,
    val logo_path: String ?= "",
    val name: String ?= "",
    val origin_country: String ?= ""
)