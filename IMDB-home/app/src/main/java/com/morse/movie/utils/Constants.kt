package com.morse.movie.utils

const val MOVIE_TYPE = "movieType"
const val MOVIE_ID_kEY = "movieId"
const val MOVIE_DETAIL_ID_kEY = "movieDetailId"
const val apiKey = "12ae0210d107863fd1d89b1e2ee1f26a"
const val baseApi = "https://api.themoviedb.org/3/"
const val searchMovies ="search/movie?api_key=$apiKey&language=en-US" //search/movie?api_key=$apiKey&language=en-US&query=asdasdasd
const val popularMovies = "movie/popular?api_key=$apiKey&language=en-US"
const val topRatedMovies = "movie/top_rated?api_key=$apiKey&language=en-US"
const val inCommingMovies = "movie/upcoming?api_key=$apiKey&language=en-US"
const val nowplayingMovies = "movie/now_playing?api_key=$apiKey&language=en-US"
const val similarMovies = "movie/{movie_id}/similar?api_key=$apiKey&language=en-US&page=1"
const val movieDetail = "movie/{movie_id}?api_key=$apiKey&language=en-US"
const val movieReview = "movie/{movie_id}/reviews?api_key=$apiKey"
const val movieVideos = "movie/{movie_id}/videos?api_key=$apiKey"
const val emptyImagePlaceHolder ="https://ui-avatars.com/api/?size=128?bold=true&background=D72027&color=FFFFFF&name="
const val youtubeVideos = "https://www.youtube.com/watch?v=" // use Key not id
const val youtubeThumbnailImage = "https://i3.ytimg.com/vi/"
//-------------------------------UnUsed--------------------------------------------------------------------------------
const val personProfile = "person/{person_id}?api_key=$apiKey&language=en-US"
const val popularTv = "tv/popular?api_key=$apiKey&language=en-US"
const val topRatedTv = "tv/top_rated?api_key=$apiKey&language=en-US&page=1"
const val inCommingTv = "tv/upcoming?api_key=$apiKey&language=en-US&page=1"
const val nowplayingTv = "tv/now_playing?api_key=$apiKey&language=en-US&page=1"

// Add w500 to be Background or w300 and then add posterPath
const val imageApiPoster = "https://image.tmdb.org/t/p/w400"
const val imageApiBackground = "https://image.tmdb.org/t/p/w500"
//Filanlly Upload Apk into : https://apkpure.com/ar/submit-apk
const val movieObjectTable = "MOVIE_Object_TABLE"
const val DATABASE_NAME = "IMDB_DB"
const val moviePrimaryKeyName = "MOVIE_ID"
const val FLURRY_API_KEY = "2HHXNKMY37BSBNRXTHZK"
//Errors
const val noNetworkExist = "Unable to resolve host api.themoviedb.org: No address associated with hostname"