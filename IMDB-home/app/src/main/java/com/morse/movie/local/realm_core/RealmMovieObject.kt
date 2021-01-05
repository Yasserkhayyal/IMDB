package com.morse.movie.local.realm_core

import io.realm.RealmObject

open class RealmMovieObject(

    public open var adult: Boolean ?=false,
    public open var backdrop_path: String ?= "",
    public open var budget: Int?= 0,
    public open var homepage: String?= "",
    @io.realm.annotations.PrimaryKey
    public open var id: Int?= 0,
    public open var imdb_id: String?= "",
    public open var original_language: String?= "",
    public open var original_title: String?= "",
    public open var overview: String?= "",
    public open var popularity: Double?= 0.0,
    public open var poster_path: String?= "",
    public open var release_date: String?= "",
    public open var revenue: Int?= 0,
    public open var runtime: Int?= 0,
    public open var status: String?= "",
    public open var tagline: String?= "",
    public open var title: String?= "",
    public open var video: Boolean ?=false,
    public open var vote_average: Double ?= 0.0,
    public open var vote_count: Int ?= 0

): RealmObject() {

}
