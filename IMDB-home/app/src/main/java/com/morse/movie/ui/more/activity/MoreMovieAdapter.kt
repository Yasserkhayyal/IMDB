package com.morse.movie.ui.more.activity

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morse.movie.R
import com.morse.movie.base.DiffUtils
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_item_horizontal.*
import java.lang.Exception


class MoreMovieAdapter (private var movieListeners: MovieListener , private var colorUtils: ColorUtils) : PagedListAdapter<Result, MoreMovieAdapter.MovieViewHolder>(DiffUtils<Result>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item_horizontal , parent , false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if(position == 0){
            var i = 1 + 2
        }
        holder.bind(getItem(position)!! , movieListeners , colorUtils?.getRandomTheme())
    }

    inner class MovieViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {

        fun bind (movie : Result , movieListeners : MovieListener , color : Color){
            loading?.makeItOn()
            movieHorizontalCardBackground?.background = color?.drawableRes
            movieFrameMoreView?.setImageDrawable(color?.frameRes)
            movieName?.setText(movie?.title)
            movieDate?.setText(movie?.release_date?.bindFromDateForMovieReleqse())
            movieImage?.loadImage(movie?.poster_path , movie?.original_title , loading)
           progressValue?.setText("${movie?.vote_average}")
            progress?.setProgress(movie?.vote_average?.toInt()!!)
            movieCard?.setOnClickListener {
                movieListeners?.onMovieClicks(it , movie , color?.colorRes)
            }
        }

    }

}