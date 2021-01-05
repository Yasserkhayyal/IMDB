package com.morse.movie.local.room_core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse


@Database(entities = arrayOf(MovieDetailResponse::class), version =6 ,exportSchema = false )
abstract class RoomManager : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object {
        @Volatile private var instance: RoomManager? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context ,
            RoomManager::class.java, "imdb_db")
            .fallbackToDestructiveMigration()
            .build()
    }

}