package com.morse.movie.ui.favourite.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.morse.movie.R
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.ui.home.activity.MovieAdapter
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.utils.bindFromDateForMovieReleqse
import com.morse.movie.utils.loadImage
import com.morse.movie.utils.makeItOn
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.favourite_movie_layout.*

class FavouriteMoviesAdapter (private var listener: MovieListener) : RecyclerView.Adapter<FavouriteMoviesAdapter.MovieViewHolder>() {

    private var listOfResults = arrayListOf<Result>()
    private lateinit var movieListener: MovieListener

    init {
        movieListener = listener
    }

    public fun submitNewMovies(results: ArrayList<Result>) {
        if (listOfResults?.size == 0) {

            listOfResults?.addAll(results)
        } else {
            listOfResults?.clear()
            listOfResults?.addAll(results)
        }
        this?.notifyDataSetChanged()
    }

    public fun clearAllMovies() {
        listOfResults?.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        return MovieViewHolder(
                LayoutInflater.from(parent?.context)?.inflate(R.layout.favourite_movie_layout, parent, false)!!
            )

    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        holder?.bindMovieToView(listOfResults?.get(position), movieListener)

    }

    override fun getItemCount(): Int {
        return listOfResults?.size
    }

    public class MovieViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {


        public fun bindMovieToView(result: Result, listener: MovieListener) {
            loading?.makeItOn()
            movieName?.setText(result?.title)
            movieDate?.setText(result?.release_date?.bindFromDateForMovieReleqse())
            movieImage?.loadImage(result?.poster_path, result?.original_title, loading)
            progressValue?.setText("${result?.vote_average}")
            progress?.setProgress(result?.vote_average?.toInt()!!)
            movieCard?.setOnClickListener {
                listener?.onMovieClicks(it, result)
            }

        }


    }

}