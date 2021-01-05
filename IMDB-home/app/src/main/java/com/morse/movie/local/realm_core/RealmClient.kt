package com.morse.movie.local.realm_core

import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.local.LocalInterface
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration
import java.lang.Exception

class RealmClient() : LocalInterface {

    var realmInstance: Realm? = null

    init {


    }

    override fun checkIfMovieExistInDataBase(movieId: Int): Observable<Boolean> {
        return Observable.create {
           try {
               realmInstance = Realm.getDefaultInstance()
               var result = realmInstance?.where(RealmMovieObject::class.java)?.equalTo("id" , movieId)?.findFirst()
               if(result == null) {
                   it?.onNext(false)
               }
               else {
                   it?.onNext(true)
               }
           }catch (e : Exception){
               it?.onError(e)
           }
        }
    }

    override fun selectAllMoviesFromDataBase(): Observable<List<MovieDetailResponse>> {
        return Observable.create {
            try {
                realmInstance = Realm.getDefaultInstance()
                var result = realmInstance?.where(RealmMovieObject::class.java)?.findAll()
                var newResult = (realmInstance?.copyFromRealm(result) as ArrayList<RealmMovieObject>) ?.map {
                    MovieDetailResponse(adult = it?.adult ,backdrop_path = it?.backdrop_path , budget = it?.budget , homepage = it?.homepage , id = it?.id , imdb_id = it?.imdb_id,
                        original_language = it?.original_language , original_title = it?.original_title ,overview = it?.overview ,popularity = it?.popularity ,poster_path = it?.poster_path ,
                        release_date = it?.release_date , revenue = it?.revenue , runtime = it?.runtime , status = it?.status ,tagline = it?.tagline , title = it?.title , video = it?.video , vote_count = it?.vote_count , vote_average = it?.vote_average)
                }
                it?.onNext(newResult)
            }catch (e: Exception) {
                it?.onError(e)
            }
        }
    }

    override fun addMovieIntoDataBase(movieDetailResponse: MovieDetailResponse): Observable<Boolean> {
        return Observable.create {
            try {
                realmInstance = Realm.getDefaultInstance()
                realmInstance?.beginTransaction()
                var movie = RealmMovieObject(
                    adult = movieDetailResponse?.adult,
                    backdrop_path = movieDetailResponse?.backdrop_path,
                    budget = movieDetailResponse?.budget,
                    homepage = movieDetailResponse?.homepage,
                    id = movieDetailResponse?.id,
                    imdb_id = movieDetailResponse?.imdb_id,
                    original_language = movieDetailResponse?.original_language,
                    original_title = movieDetailResponse?.original_title,
                    overview = movieDetailResponse?.overview,
                    popularity = movieDetailResponse?.popularity,
                    poster_path = movieDetailResponse?.poster_path,
                    release_date = movieDetailResponse?.release_date,
                    revenue = movieDetailResponse?.revenue,
                    runtime = movieDetailResponse?.runtime,
                    status = movieDetailResponse?.status,
                    tagline = movieDetailResponse?.tagline,
                    title = movieDetailResponse?.title,
                    video = movieDetailResponse?.video,
                    vote_count = movieDetailResponse?.vote_count,
                    vote_average = movieDetailResponse?.vote_average
                )
                realmInstance?.copyToRealm(movie)
                realmInstance?.commitTransaction()
                it?.onNext(true)
            }catch (e : Exception){
                it?.onError(e)
            }
        }
    }

    override fun removeMovieIntoDataBase(movieId: Int): Observable<Boolean> {
        return Observable.create {
            try {
                realmInstance = Realm.getDefaultInstance()
                realmInstance?.beginTransaction()
                var result = realmInstance?.where(RealmMovieObject :: class.java)?.equalTo("id", movieId)?.findAll()
                result?.deleteAllFromRealm()
                realmInstance?.commitTransaction()

            } catch (e: Exception) {
                it?.onError(e)
            }
        }
    }
}