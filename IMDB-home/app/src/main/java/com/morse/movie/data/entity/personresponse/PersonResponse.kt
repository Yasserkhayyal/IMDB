package com.morse.movie.data.entity.personresponse


import com.google.gson.annotations.SerializedName

data class PersonResponse(
    @SerializedName("adult")
    var adult: Boolean? = null, // false
    @SerializedName("also_known_as")
    var alsoKnownAs: List<String>? = null,
    @SerializedName("biography")
    var biography: String? = null, // Peter Wilton Cushing, OBE  (26 May 1913 – 11 August 1994) was an English actor, known for his many appearances in Hammer Films, in which he played Baron Frankenstein and Dr. Van Helsing, amongst many other roles, often appearing opposite Christopher Lee, and occasionally Vincent Price. A familiar face on both sides of the Atlantic, his most famous roles outside of "Hammer Horror" include his many appearances as Sherlock Holmes, as Grand Moff Tarkin in Star Wars (1977) and as the mysterious Doctor Who in Doctor Who and the Daleks and Daleks - Invasion Earth 2150 AD in 1965 and 1966, two cinema films based on the television series Doctor Who.Description above from the Wikipedia article Peter Cushing, licensed under CC-BY-SA, full list of contributors on Wikipedia.
    @SerializedName("birthday")
    var birthday: String? = null, // 1913-05-26
    @SerializedName("deathday")
    var deathday: String? = null, // 1994-08-11
    @SerializedName("gender")
    var gender: Int? = null, // 2
    @SerializedName("homepage")
    var homepage: Any? = null, // null
    @SerializedName("id")
    var id: Int? = null, // 5
    @SerializedName("imdb_id")
    var imdbId: String? = null, // nm0001088
    @SerializedName("known_for_department")
    var knownForDepartment: String? = null, // Acting
    @SerializedName("name")
    var name: String? = null, // Peter Cushing
    @SerializedName("place_of_birth")
    var placeOfBirth: String? = null, // Kenley, Surrey, England, UK
    @SerializedName("popularity")
    var popularity: Double? = null, // 4.305
    @SerializedName("profile_path")
    var profilePath: String? = null // /rxfWFGJm35qJb2jy0jlauhYeNgV.jpg
)