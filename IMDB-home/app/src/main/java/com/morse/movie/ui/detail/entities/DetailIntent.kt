package com.morse.movie.ui.detail.entities

import com.morse.movie.base.MviIntent
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.ui.favourite.entities.FavouriteIntent

sealed class DetailIntent  : MviIntent{

    public data class LoadMovieDetails (var movieId : Int) : DetailIntent()

    public data class LoadSimilarMovies (var movieId : Int) : DetailIntent()

    public data class LoadMovieReviewsIntent (var movieId : Int) : DetailIntent()

    public data class LoadMovieVideosIntent (var movieId : Int) : DetailIntent()

    public data class IsMovieExistInDatabaseIntent (var movieId : Int) : DetailIntent()

    public data class AddMovieToFavouriteIntent (public val movie: MovieDetailResponse) : DetailIntent()

    public data class RemoveMovieFromFavouriteIntent (public val movieId : Int) : DetailIntent()

    public data class LoadUserProfileIntent (public val userId : String) : DetailIntent()

}