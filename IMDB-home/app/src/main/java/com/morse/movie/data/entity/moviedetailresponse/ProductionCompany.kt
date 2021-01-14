package com.morse.movie.data.entity.moviedetailresponse

import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    @SerializedName("id")
    val id: Int ?= 0,
    @SerializedName("logo_path")
    val logo_path: String ?= "",
    @SerializedName("name")
    val name: String ?= "",
    @SerializedName("origin_country")
    val origin_country: String ?= ""
)