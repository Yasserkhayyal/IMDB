package com.morse.movie.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.domain.repository.DataRepository
import com.morse.movie.remote.datasource.manager.MoreDataSourceManager
import io.reactivex.Observable
import java.lang.Exception

class SearchOnMoviesUseCase( private var repository : DataRepository)  {

    public fun execute (searchText : String) : Observable<LiveData<PagedList<Result>>> {
        return repository?.searchOnMoviesByPagination(searchText)
    }

}