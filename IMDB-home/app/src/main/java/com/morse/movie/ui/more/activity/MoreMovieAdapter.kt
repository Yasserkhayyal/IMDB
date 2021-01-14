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


class MoreMovieAdapter (private var movieListeners: MovieListener) : PagedListAdapter<Result, MoreMovieAdapter.MovieViewHolder>(DiffUtils<Result>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item_horizontal , parent , false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if(position == 0){
            var i = 1 + 2
        }
        holder.bind(getItem(position)!! , movieListeners)
    }

    inner class MovieViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {

        private var listOfThemes = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                arrayListOf<Pair<Drawable , Int>>( Pair( containerView?.context?.getDrawable(R.drawable.movie_card_bg_except_left_corners )!! ,  containerView?.context?.getColor(R.color.colorOfGreen )!!)
                    , Pair( containerView?.context?.getDrawable(R.drawable.movie_card_bg_except_left_corners_amber )!! ,  containerView?.context?.getColor(R.color.colorOfAmber )!! )
                    , Pair( containerView?.context?.getDrawable(R.drawable.movie_card_bg_except_left_corners_color )!! ,  containerView?.context?.getColor(R.color.colorOfRedSpecial )!! )
                    , Pair( containerView?.context?.getDrawable(R.drawable.movie_card_bg_except_left_corners_red )!! ,  containerView?.context?.getColor(R.color.colorOfBlueSpecial )!! )
                )
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        } else {
            arrayListOf<Pair<Drawable , Int>>()
        }

        fun bind (movie : Result , movieListeners : MovieListener){
            var theme = listOfThemes?.random()
            loading?.makeItOn()
            movieHorizontalCardBackground?.background = theme?.first
            movieName?.setText(movie?.title)
            movieDate?.setText(movie?.release_date?.bindFromDateForMovieReleqse())
            movieImage?.loadImage(movie?.poster_path , movie?.original_title , loading)
           progressValue?.setText("${movie?.vote_average}")
            progress?.setProgress(movie?.vote_average?.toInt()!!)
            movieCard?.setOnClickListener {
                movieListeners?.onMovieClicks(it , movie , theme?.second)
            }
        }

    }

}