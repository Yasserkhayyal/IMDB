package com.morse.movie.ui.more.entities

import com.morse.movie.base.MviIntent

sealed class MoreIntent : MviIntent {

    object LoadPopularMoviesByPagination : MoreIntent()

    object LoadTopRatedMoviesByPagination : MoreIntent()

    object LoadInComingMoviesByPagination : MoreIntent()

    object LoadNowPlayingMoviesByPagination : MoreIntent()


}