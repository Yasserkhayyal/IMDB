package com.morse.movie.ui.detail.entities

import com.morse.movie.base.MviAction
import com.morse.movie.base.MviIntent
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.ui.favourite.entities.FavouriteActions

sealed class DetailAction  : MviAction {

    public data class LoadMovieDetails (var movieId : Int) : DetailAction()

    public data class LoadSimilarMovies (var movieId : Int) : DetailAction()

    public data class LoadMovieReviewsAction (var movieId : Int) : DetailAction()

    public data class LoadMovieVideosAction (var movieId : Int) : DetailAction()

    public data class IsMovieExistInDatabaseAction (var movieId : Int) : DetailAction()

    public data class AddMovieToFavouriteAction (public val movie: MovieDetailResponse) : DetailAction()

    public data class RemoveMovieFromFavouriteAction (public val movieId : Int) : DetailAction()

    public data class LoadUserProfileAction (public val userId : String) : DetailAction()
}