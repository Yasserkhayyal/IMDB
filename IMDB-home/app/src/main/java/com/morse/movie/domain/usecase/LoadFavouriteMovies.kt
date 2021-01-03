package com.morse.movie.domain.usecase

import com.morse.movie.domain.repository.DataRepository

class LoadFavouriteMovies ( public var repository: DataRepository) {

    public fun execute () = repository?.loadFavouriteMovies()

}