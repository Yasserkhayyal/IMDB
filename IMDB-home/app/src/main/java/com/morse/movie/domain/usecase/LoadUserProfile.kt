package com.morse.movie.domain.usecase

import com.morse.movie.data.entity.personresponse.PersonResponse
import com.morse.movie.domain.repository.DataRepository
import io.reactivex.Observable

class LoadUserProfile (val repository: DataRepository) {

    public fun execute (personId : String) : Observable<PersonResponse> {
        return repository?.loadUserProfile(personId)
    }

}