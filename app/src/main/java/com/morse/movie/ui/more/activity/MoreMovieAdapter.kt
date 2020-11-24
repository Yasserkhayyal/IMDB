package com.morse.movie.ui.more.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.morse.movie.R
import com.morse.movie.base.DiffUtils
import com.morse.movie.remote.entity.movieresponse.Result
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.utils.bindFromDateForMovieReleqse
import com.morse.movie.utils.imageApiPoster
import com.morse.movie.utils.makeItOff
import com.morse.movie.utils.makeItOn
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_item_horizontal.*
import java.lang.Exception


class MoreMovieAdapter (private var movieListeners: MovieListener) : PagedListAdapter<Result, MoreMovieAdapter.MovieViewHolder>(DiffUtils<Result>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item_horizontal , parent , false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position)!! , movieListeners)
    }

    inner class MovieViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {

        fun bind (movie : Result , movieListeners : MovieListener){
            loading?.makeItOn()
            movieName?.setText(movie?.title)
            movieDate?.setText(movie?.release_date?.bindFromDateForMovieReleqse())
            Picasso.get()?.load(imageApiPoster +movie?.poster_path)?.transform(RoundedCornersTransformation(20 , 10))?.into(movieImage, object : Callback {
                override fun onSuccess() {
                    loading?.makeItOff()
                }

                override fun onError(e: Exception?) {
                    loading?.makeItOff()
                }

            })
            progressValue?.setText("${movie?.vote_average}")
            progress?.setProgress(movie?.vote_average?.toInt()!!)
            movieCard?.setOnClickListener {
                movieListeners?.onMovieClicks(it , movie)
            }
        }

    }
}