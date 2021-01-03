package com.morse.movie.domain.usecase

import com.morse.movie.domain.repository.DataRepository

class CheckIfMovieExistInDataBase (private val repository: DataRepository) {

    public fun execute (movieId : Int) = repository?.checkIfMovieExistInDataBase(movieId)

}