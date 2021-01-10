package com.morse.movie.local.room_core

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.utils.DATABASE_NAME
import com.morse.movie.utils.moviePrimaryKeyName

@Dao
interface MovieDao {

    @Insert
    public fun addNewMovieToFavourite (movie : MovieDetailResponse)

    @Query(" SELECT Exists ( SELECT * FROM movie_object_table WHERE id == :movieId ) ")
    public fun checkIfExistInFavourite ( movieId : Int ) : Boolean

    @Query (" DELETE FROM movie_object_table WHERE id == :movieId ")
    public fun removeMovieFromFavourite (movieId: Int)

    @Query (" SELECT * FROM movie_object_table ")
    public fun selectAllMoviesFromFavourite () : List<MovieDetailResponse>

    @Query (" DELETE FROM movie_object_table ")
    public fun removeAllMovieFromFavourite ()


}