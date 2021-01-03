package com.morse.movie.ui.more.entities

import com.morse.movie.base.MviAction

sealed class MoreAction : MviAction {

    object LoadPopularMoviesByPagination : MoreAction()

    object LoadTopRatedMoviesByPagination : MoreAction()

    object LoadInComingMoviesByPagination : MoreAction()

    object LoadNowPlayingMoviesByPagination : MoreAction()

    data class SearchOnMoviesByPaginationAction (val searchText : String) : MoreAction()

}