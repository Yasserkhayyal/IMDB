package com.morse.movie.data.entity.moviereviewresponse


import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    @SerializedName("avatar_path")
    var avatarPath: String? = null, // /klZ9hebmc8biG1RC4WmzNFnciJN.jpg
    @SerializedName("name")
    var name: String? = null, // SWITCH.
    @SerializedName("rating")
    var rating: Int? = null, // 6
    @SerializedName("username")
    var username: String? = null // maketheSWITCH
)