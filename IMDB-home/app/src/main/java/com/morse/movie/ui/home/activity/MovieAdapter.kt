package com.morse.movie.ui.home.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.morse.movie.R
import com.morse.movie.base.RecyclerViewShape
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_item.*
import java.lang.Exception

class MovieAdapter (private var recyclerViewShape: RecyclerViewShape ? = RecyclerViewShape.VERTICAL,
                    private var listener: MovieListener) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var listOfResults = arrayListOf<Result>()
    private lateinit var movieListener : MovieListener

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

    public fun clearAllMovies (){
        listOfResults?.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return if(recyclerViewShape == RecyclerViewShape.HORIZONTAL){
            return MovieViewHolder(
                LayoutInflater.from(parent?.context)?.inflate(R.layout.movie_item_horizontal, parent, false)!!
            )
        } else {
            return MovieViewHolder(
                LayoutInflater.from(parent?.context)?.inflate(R.layout.movie_item, parent, false)!!
            )
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        holder?.bindMovieToView(listOfResults?.get(position) , movieListener)

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
            movieImage?.loadImage(result?.poster_path , result?.original_title , loading)
            progressValue?.setText("${result?.vote_average}")
            progress?.setProgress(result?.vote_average?.toInt()!!)
            movieCard?.setOnClickListener {
                listener?.onMovieClicks(it , result)
            }

        }


    }


}