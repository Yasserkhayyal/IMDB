package com.morse.movie.ui.home.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.morse.movie.R
import com.morse.movie.remote.entity.Result
import com.morse.movie.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_item.*
import kotlinx.android.synthetic.main.movie_item.view.*
import java.lang.Exception

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var listOfResults = arrayListOf<Result>()

    public fun submitNewMovies(results: ArrayList<Result>) {
        if (listOfResults?.size == 0) {

            listOfResults?.addAll(results)
        } else {
            listOfResults?.clear()
            listOfResults?.addAll(results)
        }
        this?.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent?.context)?.inflate(R.layout.movie_item, parent, false)!!
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        holder?.bindMovieToView(listOfResults?.get(position))

    }

    override fun getItemCount(): Int {
        return listOfResults?.size
    }

    public class MovieViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {


        public fun bindMovieToView(result: Result) {
            loading?.makeItOn()
            movieName?.setText(result?.title)
            movieDate?.setText(result?.release_date?.bindFromDateForMovieReleqse())
            Picasso.get()?.load(imageApiPoster+result?.poster_path)?.transform(RoundedCornersTransformation(20 , 10))?.into(movieImage, object : Callback {
                override fun onSuccess() {
                    loading?.makeItOff()
                }

                override fun onError(e: Exception?) {
                    loading?.makeItOff()
                }

            })
            progressValue?.setText("${result?.vote_average}")
            progress?.setProgress(result?.vote_average?.toInt()!!)
        }


    }


}