package com.morse.movie.ui.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.morse.movie.R
import com.morse.movie.base.RecyclerViewShape
import com.morse.movie.base.RecyclerviewListener
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.moviereviewresponse.Result
import com.morse.movie.ui.home.activity.MovieAdapter
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_item.*
import kotlinx.android.synthetic.main.person_review_layout.*
import java.lang.Exception

class ReviewAdapter(
    private var recyclerViewShape: RecyclerViewShape? = RecyclerViewShape.VERTICAL,
    private var listener: RecyclerviewListener<Result>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var listOfResults = arrayListOf<Result>()
    private lateinit var movieListener : RecyclerviewListener<Result>


    init {
        movieListener = listener
    }

    public fun submitNewReviews(results: ArrayList<Result>) {
        if (listOfResults?.size == 0) {
            listOfResults?.addAll(results)
        } else {
            listOfResults?.clear()
            listOfResults?.addAll(results)
        }
        this?.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewAdapter.ReviewViewHolder(
            LayoutInflater.from(parent?.context)
                ?.inflate(R.layout.person_review_layout, parent, false)!!
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder?.bindReviewToView(listOfResults?.get(position) , movieListener)
    }

    override fun getItemCount(): Int {
        return listOfResults?.size
    }

    public class ReviewViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {

        public fun bindReviewToView(result: Result, listener: RecyclerviewListener<Result>) {
            detailPosterImageLoading?.makeItOn()
            personNameTextView?.setText(result?.authorDetails?.username)
            personReviewTextView?.setText(result?.content)
            personImageView?.loadUserImage(result?.authorDetails?.avatarPath , result?.author , detailPosterImageLoading)
            personImageView?.setOnClickListener {
                listener?.onItemPressed(personImageView , result)
            }

        }

    }

}