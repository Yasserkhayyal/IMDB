package com.morse.movie.ui.home.entities

import com.morse.movie.base.MviAction

sealed class HomeAction : MviAction{

    object LoadPopularMovies : HomeAction()

    object LoadTopRatedMovies : HomeAction()

    object LoadIncomingMovies : HomeAction()

    object LoadNowPlayingMovies : HomeAction()

}