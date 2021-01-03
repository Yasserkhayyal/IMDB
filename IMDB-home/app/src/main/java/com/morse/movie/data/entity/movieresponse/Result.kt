package com.morse.movie.data.entity.movieresponse

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morse.movie.utils.movieObjectTable
import com.morse.movie.utils.moviePrimaryKeyName

data class Result(
    val adult: Boolean ?=false,
    val backdrop_path: String?= "",
    var id: Int?= 0,
    val original_language: String?= "",
    val original_title: String?= "",
    val overview: String?= "",
    val popularity: Double?= 0.0,
    val poster_path: String?= "",
    val release_date: String?= "",
    val title: String?= "",
    val video: Boolean?=false,
    val vote_average: Double?= 0.0,
    val vote_count: Int ?= 0
)