package com.morse.movie.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.domain.repository.DataRepository
import com.morse.movie.remote.datasource.manager.MoreDataSourceManager
import io.reactivex.Observable
import java.lang.Exception

class LoadPaginationMovies ( val repository : DataRepository) {

    public fun execute (position : Int) : Observable<LiveData<PagedList<Result>>>{
        return when(position) {
            1 -> return repository?.loadPopularMoviesByPagination()
            2 -> return repository?.loadTopRatedMoviesByPagination()
            3 -> return repository?.loadInComingMoviesByPagination()
            4 -> return repository?.loadNowPlayingMoviesByPagination()
            else -> return repository?.loadPopularMoviesByPagination()
        }
    }

}