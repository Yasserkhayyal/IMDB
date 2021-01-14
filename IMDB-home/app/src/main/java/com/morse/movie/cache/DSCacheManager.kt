package com.morse.movie.cache

import com.morse.movie.data.cache.CacheInterface
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse

class DSCacheManager  : CacheInterface{

    private val MOVIE_SIZE = 15
    private val SIMILAR_MOVIE_SIZE = 5
    private val DETAIL_SIZE = 5
    private val REVIEWS_SIZE = 5
    private val VIDEOS_SIZE = 5
    private var popularMoviesCache = HashMap<String , MovieResponse>()
    private var topRatedMoviesCache = HashMap<String , MovieResponse>()
    private var inComingMoviesCache = HashMap<String , MovieResponse>()
    private var nowPlayingMoviesCache = HashMap<String , MovieResponse>()
    private var similarMoviesCache = HashMap<String , MovieResponse>()
    private var movieDetailsCache = HashMap<String , MovieDetailResponse>()
    private var videosCache = HashMap<String , MovieVideoResponse>()
    private var reviewsCache = HashMap<String , MovieReview>()

    private constructor(){

    }

    companion object {

        private var singeltonDSCacheManager : DSCacheManager ? = null

        public fun  newInstance() : DSCacheManager {
            return  if (singeltonDSCacheManager == null) {
                singeltonDSCacheManager = DSCacheManager()
                return singeltonDSCacheManager!!
            }
            else {
                return singeltonDSCacheManager!!
            }
        }

    }

    //------------------------------------- Check Data Existance -----------------------------------

    public override fun isExistInPopularCache (pageNumber : String)  :Boolean{
        return popularMoviesCache?.containsKey(pageNumber)
    }

    public  override fun isExistInTopRatedCache (pageNumber : String)  :Boolean{
        return topRatedMoviesCache?.containsKey(pageNumber)
    }

    public  override fun isExistInInComingCache (pageNumber : String)  :Boolean{
        return inComingMoviesCache?.containsKey(pageNumber)
    }

    public override fun isExistInNowPlayingCache (pageNumber : String)  :Boolean{
        return nowPlayingMoviesCache?.containsKey(pageNumber)
    }

    public override fun isExistInDetailCache (movieId : String) : Boolean {
        return movieDetailsCache?.containsKey(movieId)
    }

    public override fun isExistInReviewsCache (movieId : String) : Boolean {
        return reviewsCache?.containsKey(movieId)
    }

    public override fun isExistInVideosCache (movieId : String) : Boolean {
        return videosCache?.containsKey(movieId)
    }

    override fun isExistInSimilarCache(movieId: String): Boolean {
        return similarMoviesCache?.containsKey(movieId)
    }

    //------------------------------------- Load Data ----------------------------------------------

    public override fun getPopularMoviesFromCache (pageNumber : String) : MovieResponse? {
        return popularMoviesCache?.get(pageNumber)
    }

    public override fun getTopRatedMoviesFromCache (pageNumber : String) : MovieResponse? {
        return topRatedMoviesCache?.get(pageNumber)
    }

    public override fun getInComingMoviesFromCache (pageNumber : String) : MovieResponse? {
        return inComingMoviesCache?.get(pageNumber)
    }

    public override fun getNowPlayingMoviesFromCache (pageNumber : String) : MovieResponse? {
        return nowPlayingMoviesCache?.get(pageNumber)
    }

    public override fun getDetailMovieFromCache (movieId : String) : MovieDetailResponse? {
        return movieDetailsCache?.get(movieId)
    }

    public override fun getVideosMovieFromCache (movieId: String) : MovieVideoResponse? {
        return videosCache?.get(movieId)
    }

    public override fun getReviewsMovieFromCache (movieId: String) : MovieReview? {
        return reviewsCache?.get(movieId)
    }

    override fun getSimilarMoviesFromCache(movieId: String): MovieResponse? {
        return similarMoviesCache?.get(movieId)
    }

    //------------------------------------- Add Data ----------------------------------------------

    public override fun addNewPopularMovieIntoCache (pageNumber : String , movieResponse: MovieResponse) {
        if ( popularMoviesCache executeMovieCondition(MOVIE_SIZE) == true ) {
            popularMoviesCache?.clear()
            popularMoviesCache?.put(pageNumber , movieResponse)
        }
        else {
            popularMoviesCache?.put(pageNumber , movieResponse)
        }
    }

    public  override fun addNewTopRatedMovieIntoCache (pageNumber : String , movieResponse: MovieResponse) {

        if(topRatedMoviesCache executeMovieCondition (MOVIE_SIZE) == true ) {
            topRatedMoviesCache?.clear()
            topRatedMoviesCache?.put(pageNumber , movieResponse)
        }
        else {
            topRatedMoviesCache?.put(pageNumber , movieResponse)
        }

    }

    public override fun addNewInComingMovieIntoCache (pageNumber : String , movieResponse: MovieResponse) {
        if(inComingMoviesCache executeMovieCondition (MOVIE_SIZE) == true ) {
            inComingMoviesCache?.clear()
            inComingMoviesCache?.put(pageNumber , movieResponse)
        }
        else {
            inComingMoviesCache?.put(pageNumber , movieResponse)
        }
    }

    public override fun addNewNowPlayingMovieIntoCache (pageNumber : String , movieResponse: MovieResponse) {
        if(nowPlayingMoviesCache executeMovieCondition (MOVIE_SIZE) == true ) {
            nowPlayingMoviesCache?.clear()
            nowPlayingMoviesCache?.put(pageNumber , movieResponse)
        }
        else {
            nowPlayingMoviesCache?.put(pageNumber , movieResponse)
        }
    }

    public override fun addNewMovieDetailIntoCache (movieId : String , movieDetailResponse: MovieDetailResponse){
        if(movieDetailsCache executeDetailCondition  (DETAIL_SIZE) == true ) {
            movieDetailsCache?.clear()
            movieDetailsCache?.put(movieId , movieDetailResponse)
        }
        else {
            movieDetailsCache?.put(movieId , movieDetailResponse)
        }

    }

    public override fun addNewReviewsIntoCache (movieId: String , reviewsResponse: MovieReview){

        if(reviewsCache executeReviewsCondition (REVIEWS_SIZE) == true ) {
            reviewsCache?.clear()
            reviewsCache?.put(movieId , reviewsResponse)
        }
        else {
            reviewsCache?.put(movieId , reviewsResponse)
        }

    }

    public override fun addNewVideoIntoCache (movieId: String , videoResponse: MovieVideoResponse){


        if(videosCache executeVideoCondition  (VIDEOS_SIZE) == true ) {
            videosCache?.clear()
            videosCache?.put(movieId , videoResponse)
        }
        else {
            videosCache?.put(movieId , videoResponse)
        }

    }

    override fun addNewSimilarMovieIntoCache(movieId: String, movieResponse: MovieResponse) {
        if ( similarMoviesCache executeSimilarMovieCondition (SIMILAR_MOVIE_SIZE) == true ) {
            similarMoviesCache?.clear()
            similarMoviesCache?.put(movieId , movieResponse)
        }
        else {
            similarMoviesCache?.put(movieId , movieResponse)
        }
    }

    //------------------------------------- Check Data Length --------------------------------------

    public override infix fun HashMap<String , MovieResponse >.executeMovieCondition ( size : Int ) : Boolean {
        return if (this?.size >= size)  true else false
    }

    public override infix fun HashMap<String , MovieVideoResponse >.executeVideoCondition ( size : Int ) : Boolean {
        return if (this?.size >= size)  true else false
    }

    public override infix fun HashMap<String , MovieDetailResponse >.executeDetailCondition ( size : Int ) : Boolean {
        return if (this?.size >= size)  true else false
    }

    public override infix fun HashMap<String , MovieReview >.executeReviewsCondition ( size : Int ) : Boolean {
        return if (this?.size >= size)  true else false
    }

    override fun HashMap<String, MovieResponse>.executeSimilarMovieCondition(size: Int): Boolean {
        return if (this?.size >= size)  true else false
    }
}