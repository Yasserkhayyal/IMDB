package com.morse.movie.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.domain.repository.DataRepository
import io.reactivex.Observable

class SearchOnMoviesUseCase( private var repository : DataRepository)  {

    public fun execute (searchText : String) : Observable<LiveData<PagedList<Result>>> {
        return repository?.searchOnMoviesByPagination(searchText)
    }

}