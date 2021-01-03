package com.morse.movie.local.manager

import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.local.LocalInterface
import com.morse.movie.local.room_core.RoomManager
import com.morse.movie.ui.detail.entities.DetailResult
import com.morse.movie.ui.favourite.entities.FavouriteResult
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RoomClient(public var roomDatabase: RoomManager) : LocalInterface {


    override fun checkIfMovieExistInDataBase(movieId: Int): Observable<Boolean> {

        return Observable.create<Boolean> {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var result = roomDatabase?.getMovieDao()?.checkIfExistInFavourite(movieId)
                    it?.onNext(result)
                }
            }catch ( e : Exception){
                it?.onError(e)
            }

        }
    }

    override fun selectAllMoviesFromDataBase(): Observable<List<MovieDetailResponse>> {
        return Observable.create<List<MovieDetailResponse>> {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var result = roomDatabase?.getMovieDao()?.selectAllMoviesFromFavourite()
                    it?.onNext(result)
                }

            }catch (e : Exception) {
                it?.onError(e)
            }
        }
    }

    override fun addMovieIntoDataBase(movieDetailResponse: MovieDetailResponse): Observable<Boolean> {
        return Observable.create<Boolean> {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var result =
                        roomDatabase?.getMovieDao()?.addNewMovieToFavourite(movieDetailResponse)
                    it?.onNext(true)
                }
            } catch (e: Exception) {
                it?.onError(e)
            }

        }
    }

    override fun removeMovieIntoDataBase(movieId: Int): Observable<Boolean> {
        return Observable.create<Boolean> {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var result = roomDatabase?.getMovieDao()?.removeMovieFromFavourite(movieId)
                    it?.onNext(true)
                }
            } catch (e: Exception) {
                it?.onError(e)
            }

        }
    }
}