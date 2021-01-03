package com.morse.movie.data.entity.moviedetailresponse

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morse.movie.utils.movieObjectTable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = movieObjectTable)
data class MovieDetailResponse(
    val adult: Boolean ?=false,
    val backdrop_path: String ?= "",
    val budget: Int?= 0,
    val homepage: String?= "",
    @PrimaryKey(autoGenerate = false)
    val id: Int?= 0,
    val imdb_id: String?= "",
    val original_language: String?= "",
    val original_title: String?= "",
    val overview: String?= "",
    val popularity: Double?= 0.0,
    val poster_path: String?= "",
    val release_date: String?= "",
    val revenue: Int?= 0,
    val runtime: Int?= 0,
    val status: String?= "",
    val tagline: String?= "",
    val title: String?= "",
    val video: Boolean ?=false,
    val vote_average: Double ?= 0.0,
    val vote_count: Int ?= 0
) : Parcelable