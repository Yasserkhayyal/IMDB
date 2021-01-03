package com.morse.movie.data.entity.moviereviewresponse


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("author")
    var author: String? = null, // SWITCH.
    @SerializedName("author_details")
    var authorDetails: AuthorDetails? = null,
    @SerializedName("content")
    var content: String? = null, // With cinemas starving for content - especially family content these school holidays - 'The Croods: A New Age' is your best bet. It's the perfect holiday escape for the whole family, with big laugh-out-loud moments, epic fight sequences, and of course a great message about getting along and working together.- Chris dos SantosRead Chris' full article...https://www.maketheswitch.com.au/article/review-the-croods-a-new-age-a-fun-family-prehistoric-escape
    @SerializedName("created_at")
    var createdAt: String? = null, // 2020-12-10T23:46:59.538Z
    @SerializedName("id")
    var id: String? = null, // 5fd2b373f92532003daae946
    @SerializedName("updated_at")
    var updatedAt: String? = null, // 2020-12-19T18:43:35.679Z
    @SerializedName("url")
    var url: String? = null // https://www.themoviedb.org/review/5fd2b373f92532003daae946
)