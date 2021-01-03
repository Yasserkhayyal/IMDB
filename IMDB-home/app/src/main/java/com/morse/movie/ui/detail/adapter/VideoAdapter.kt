package com.morse.movie.ui.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.morse.movie.data.entity.movievideosresponse.Result
import androidx.recyclerview.widget.RecyclerView
import com.morse.movie.R
import com.morse.movie.base.RecyclerViewShape
import com.morse.movie.base.RecyclerviewListener
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_video_layout.*
import kotlinx.android.synthetic.main.person_review_layout.*

class VideoAdapter (private var recyclerViewShape: RecyclerViewShape? = RecyclerViewShape.VERTICAL,
                    private var listener: RecyclerviewListener<Result>
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>()  {

    private var listOfResults = arrayListOf<Result>()
    private lateinit var movieListener : RecyclerviewListener<Result>

    init {
        movieListener = listener
    }

    public fun submitNewVideos(results: ArrayList<Result>) {
        if (listOfResults?.size == 0) {
            listOfResults?.addAll(results)
        } else {
            listOfResults?.clear()
            listOfResults?.addAll(results)
        }
        this?.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoAdapter.VideoViewHolder(
            LayoutInflater.from(parent?.context)
                ?.inflate(R.layout.movie_video_layout, parent, false)!!
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder?.bindVideoToView(listOfResults?.get(position) , listener)
    }

    override fun getItemCount(): Int {
        return listOfResults?.size
    }

    public class VideoViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {

        public fun bindVideoToView(result: Result, listener: RecyclerviewListener<Result>) {
            videoImageLoading?.makeItOn()
            playMovieButton?.hideVisibilty()
            val url = youtubeThumbnailImage + result?.key + "/maxresdefault.jpg"
            Picasso.get()?.load(url)?.transform(
                RoundedCornersTransformation(20 , 10)
            )?.into(youtubeVideoPlayer, object : Callback {
                override fun onSuccess() {
                    videoImageLoading?.makeItOff()
                    playMovieButton?.showVisibilty()
                }

                override fun onError(e: Exception?) {
                    videoImageLoading?.makeItOff()
                    playMovieButton?.showVisibilty()
                }

            })
            videoCard?.setOnClickListener {
                listener?.onItemPressed(it , result)
            }

            playMovieButton?.setOnClickListener {
                listener?.onItemPressed(it , result)
            }

        }

    }

}