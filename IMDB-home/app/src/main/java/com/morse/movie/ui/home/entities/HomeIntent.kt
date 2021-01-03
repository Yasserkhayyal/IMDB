package com.morse.movie.ui.home.entities

import com.morse.movie.base.MviIntent

sealed class HomeIntent : MviIntent{

    object LoadPopularMovies : HomeIntent()

    object LoadTopRatedMovies : HomeIntent()

    object LoadIncomingMovies : HomeIntent()

    object LoadNowPlayingMovies : HomeIntent()

}