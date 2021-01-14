package com.morse.movie.data.entity.moviedetailresponse

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.utils.movieObjectTable
import io.realm.RealmObject
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = movieObjectTable)
data class MovieDetailResponse(
    @SerializedName("adult")
    val adult: Boolean ?=false,
    @SerializedName("backdrop_path")
    val backdrop_path: String ?= "",
    @SerializedName("budget")
    val budget: Int?= 0,
    @SerializedName("homepage")
    val homepage: String?= "",
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int?= 0,
    @SerializedName("imdb_id")
    val imdb_id: String?= "",
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
    @SerializedName("revenue")
    val revenue: Int?= 0,
    @SerializedName("runtime")
    val runtime: Int?= 0,
    @SerializedName("status")
    val status: String?= "",
    @SerializedName("tagline")
    val tagline: String?= "",
    @SerializedName("title")
    val title: String?= "",
    @SerializedName("video")
    val video: Boolean ?=false,
    @SerializedName("vote_average")
    val vote_average: Double ?= 0.0,
    @SerializedName("vote_count")
    val vote_count: Int ?= 0
) : Parcelable

class MovieDetailResponseDeserializer : ResponseDeserializable<MovieDetailResponse> {

    override fun deserialize(content: String) =
        Gson().fromJson<MovieDetailResponse>(content, MovieDetailResponse::class.java)
}