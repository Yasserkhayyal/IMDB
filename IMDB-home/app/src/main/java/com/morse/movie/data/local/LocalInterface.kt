package com.morse.movie.data.local

import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.ui.detail.entities.DetailResult
import com.morse.movie.ui.favourite.entities.FavouriteResult
import io.reactivex.Observable

interface LocalInterface {

    public fun checkIfMovieExistInDataBase ( movieId : Int ) : Observable<Boolean>

    public fun selectAllMoviesFromDataBase () : Observable< List<MovieDetailResponse> >

    public fun addMovieIntoDataBase (movieDetailResponse: MovieDetailResponse) : Observable<Boolean>

    public fun removeMovieIntoDataBase ( movieId : Int ) : Observable<Boolean>

}