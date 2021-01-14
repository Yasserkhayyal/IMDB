package com.morse.movie.data.cache

import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse

interface CacheInterface {


    public fun isExistInPopularCache (pageNumber : String)  :Boolean

    public fun isExistInTopRatedCache (pageNumber : String)  :Boolean

    public fun isExistInInComingCache (pageNumber : String)  :Boolean

    public fun isExistInNowPlayingCache (pageNumber : String)  :Boolean

    public fun isExistInDetailCache (movieId : String) : Boolean

    public fun isExistInReviewsCache (movieId : String) : Boolean

    public fun isExistInSimilarCache (movieId : String) : Boolean

    public fun isExistInVideosCache (movieId : String) : Boolean

    public fun getPopularMoviesFromCache (pageNumber : String) : MovieResponse?

    public fun getTopRatedMoviesFromCache (pageNumber : String) : MovieResponse?

    public fun getInComingMoviesFromCache (pageNumber : String) : MovieResponse?

    public fun getNowPlayingMoviesFromCache (pageNumber : String) : MovieResponse?

    public fun getSimilarMoviesFromCache (movieId : String) : MovieResponse?

    public fun getDetailMovieFromCache (movieId : String) : MovieDetailResponse?

    public fun getVideosMovieFromCache (movieId: String) : MovieVideoResponse?

    public fun getReviewsMovieFromCache (movieId: String) : MovieReview?

    public fun addNewPopularMovieIntoCache (pageNumber : String , movieResponse: MovieResponse)

    public fun addNewTopRatedMovieIntoCache (pageNumber : String , movieResponse: MovieResponse)

    public fun addNewInComingMovieIntoCache (pageNumber : String , movieResponse: MovieResponse)

    public fun addNewNowPlayingMovieIntoCache (pageNumber : String , movieResponse: MovieResponse)

    public fun addNewSimilarMovieIntoCache (movieId : String , movieResponse: MovieResponse)

    public fun addNewMovieDetailIntoCache (movieId : String , movieDetailResponse: MovieDetailResponse)

    public fun addNewReviewsIntoCache (movieId: String , reviewsResponse: MovieReview)

    public fun addNewVideoIntoCache (movieId: String , videoResponse: MovieVideoResponse)

    public infix fun HashMap<String , MovieResponse >.executeSimilarMovieCondition ( size : Int ) : Boolean

    public infix fun HashMap<String , MovieResponse >.executeMovieCondition ( size : Int ) : Boolean

    public infix fun HashMap<String , MovieVideoResponse >.executeVideoCondition ( size : Int ) : Boolean

    public infix fun HashMap<String , MovieDetailResponse >.executeDetailCondition ( size : Int ) : Boolean

    public infix fun HashMap<String , MovieReview >.executeReviewsCondition ( size : Int ) : Boolean


}