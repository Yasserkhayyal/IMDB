package com.morse.movie.ui.detail.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.morse.movie.R
import com.morse.movie.remote.entity.movieresponse.Result
import com.morse.movie.ui.home.activity.MovieAdapter
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.utils.MOVIE_ID_kEY
import com.morse.movie.utils.addDiscreteViewConfig
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var similarMoviesAdapter : MovieAdapter
    private var movieId : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        movieId = intent?.extras?.getInt(MOVIE_ID_kEY)
        Toast.makeText(this@MovieDetailActivity , "The id is ${movieId}" , Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        similarMoviesAdapter = MovieAdapter(object : MovieListener {
            override fun onMovieClicks(movieCard: View, movieResult: Result) {
               Toast.makeText(this@MovieDetailActivity , "Clicked" , Toast.LENGTH_SHORT).show()
            }
        })
        configureSimilarMovies()
    }

    private fun configureSimilarMovies (){
        similarMovieDiscreteView?.addDiscreteViewConfig()
        similarMovieDiscreteView?.adapter = similarMoviesAdapter
    }


}