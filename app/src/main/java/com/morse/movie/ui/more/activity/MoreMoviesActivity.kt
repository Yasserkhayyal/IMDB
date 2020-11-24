package com.morse.movie.ui.more.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.morse.movie.R
import com.morse.movie.utils.MOVIE_TYPE
import kotlinx.android.synthetic.main.activity_more_movies.*

class MoreMoviesActivity : AppCompatActivity() {

    private var moviePosition : Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_movies)
        moviePosition = intent?.extras?.getInt(MOVIE_TYPE)
        chooseCorrectTitle(moviePosition)
        moreActivityActionBar?.setNavigationOnClickListener {
            this.finish()
        }
    }

    private fun chooseCorrectTitle (position : Int?){
        if(position == 0){
            moreActivityActionBar?.title = "Popular Movies"
        } else if(position == 1){
            moreActivityActionBar?.title = "Top Rated Movies"
        } else if(position == 2){
            moreActivityActionBar?.title = "Incoming Movies"
        } else if(position == 3){
            moreActivityActionBar?.title = "Now Playing Movies"
        }
    }

    override fun onBackPressed() {

    }

}