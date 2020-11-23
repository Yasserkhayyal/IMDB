package com.morse.movie.utils

const val MOVIE_ID_kEY = "movieId"
const val apiKey = "12ae0210d107863fd1d89b1e2ee1f26a"
const val baseApi = "https://api.themoviedb.org/3/"
const val popularMovies = "movie/popular?api_key=$apiKey&language=en-US"
const val topRatedMovies = "movie/top_rated?api_key=$apiKey&language=en-US"
const val inCommingMovies = "movie/upcoming?api_key=$apiKey&language=en-US"
const val nowplayingMovies = "movie/now_playing?api_key=$apiKey&language=en-US"
const val similarMovies = "{movie_id}/similar?api_key=$apiKey>&language=en-US&page=1"
const val movieDetail = "/movie/{movie_id}?api_key=$apiKey&language=en-US"
const val popularPeople = "/person/popular?api_key=$apiKey&language=en-US"
const val popularTv = "tv/popular?api_key=$apiKey&language=en-US"
const val topRatedTv = "tv/top_rated?api_key=$apiKey&language=en-US&page=1"
const val movieVideos = "{movie_id}/videos?api_key=$apiKey"
const val movieReview = "/{movie_id}/reviews?api_key=$apiKey"
const val peopleInfo = "/person/{person_id}?api_key=$apiKey"

// Add w500 to be Background or w300 and then add posterPath
const val imageApiPoster = "https://image.tmdb.org/t/p/w400"
const val imageApiBackground = "https://image.tmdb.org/t/p/w700"
//Filanlly Upload Apk into : https://apkpure.com/ar/submit-apk