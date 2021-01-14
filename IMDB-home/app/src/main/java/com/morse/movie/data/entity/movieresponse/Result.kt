package com.morse.movie.data.entity.movieresponse

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.morse.movie.utils.movieObjectTable
import com.morse.movie.utils.moviePrimaryKeyName

data class Result(
    @SerializedName("adult")
    val adult: Boolean ?=false,
    @SerializedName("backdrop_path")
    val backdrop_path: String?= "",
    @SerializedName("id")
    var id: Int?= 0,
    @SerializedName("original_language")
    val original_language: String?= "",
    @SerializedName("original_title")
    val original_title: String?= "",
    @SerializedName("overview")
    val overview: String?= "",
    @SerializedName("popularity")
    val popularity: Double?= 0.0,
    @SerializedName("poster_path")
    val poster_path: String?= "",
    @SerializedName("release_date")
    val release_date: String?= "",
    @SerializedName("title")
    val title: String?= "",
    @SerializedName("video")
    val video: Boolean?=false,
    @SerializedName("vote_average")
    val vote_average: Double?= 0.0,
    @SerializedName("vote_count")
    val vote_count: Int ?= 0
)

