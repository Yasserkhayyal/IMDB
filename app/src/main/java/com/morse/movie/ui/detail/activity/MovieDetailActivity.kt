package com.morse.movie.ui.detail.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.morse.movie.R
import com.morse.movie.base.MviView
import com.morse.movie.base.RecyclerViewShape
import com.morse.movie.remote.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.remote.entity.movieresponse.MovieResponse
import com.morse.movie.remote.entity.movieresponse.Result
import com.morse.movie.ui.detail.entities.DetailIntent
import com.morse.movie.ui.detail.entities.DetailState
import com.morse.movie.ui.detail.viewmodel.DetailViewModel
import com.morse.movie.ui.home.activity.MovieAdapter
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.movie_item.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MovieDetailActivity : AppCompatActivity(), MviView<DetailIntent, DetailState> {

    private lateinit var similarMoviesAdapter: MovieAdapter
    private var movieId: Int? = 0
    private var movieDetailPublishSubject: PublishSubject<DetailIntent.LoadMovieDetails> =
        PublishSubject.create()
    private var movieDetailSimilarPublishSubject: PublishSubject<DetailIntent.LoadSimilarMovies> =
        PublishSubject.create()
    private lateinit var compositeDisposable: CompositeDisposable
    private val detailViewModel: DetailViewModel by lazy {
        ViewModelProviders.of(this).get(DetailViewModel::class.java)
    }
    private var currentMovieId : Int ?= null
    private lateinit var movieView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        movieId = intent?.extras?.getInt(MOVIE_ID_kEY)
    }

    override fun onStart() {
        super.onStart()
        similarMoviesAdapter = MovieAdapter(RecyclerViewShape.VERTICAL ,object : MovieListener {

            override fun onMovieClicks(movieCard: View, movieResult: Result) {

                currentMovieId = movieResult?.id!!
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (cardOfPopularDetail?.visibility == View.VISIBLE) {
                        returnCardToOriginPosition(200)
                        Observable.intervalRange(0, 100, 250, 0, TimeUnit.MILLISECONDS)
                            ?.subscribeOn(AndroidSchedulers.mainThread())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe {
                                animateCard(movieCard)
                                bindMovieDetailToPopularCard(movieResult)
                            }?.addTo(compositeDisposable)
                    } else {
                        animateCard(movieCard)
                        bindMovieDetailToPopularCard(movieResult)
                    }
                }
            }
        })
        cardOfPopularDetail?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(650)
            }
        }
        goToDetailOfMovieDetailDetailButton?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPositionWithNavigationAction()
            }
        }
        compositeDisposable = CompositeDisposable()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            movieDetailRoot?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if(scrollY > oldScrollY){
                    movieDetailWebsiteButton?.shrink()
                    movieDetailFavouriteButton?.shrink()
                }
                else{
                    movieDetailWebsiteButton?.extend()
                    movieDetailFavouriteButton?.extend()
                }
            }
        }
        configureSimilarMovies()
        bindOurViewWithViewModel()
        movieDetailPublishSubject?.onNext(DetailIntent.LoadMovieDetails(movieId!!))
        movieDetailSimilarPublishSubject?.onNext(DetailIntent.LoadSimilarMovies(movieId!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPositionWithNavigationAction () {
        android.transition.TransitionManager.beginDelayedTransition(
            movieDetailRoot,
            getTransform(cardOfPopularDetail, movieView , 650)
        )
        cardOfPopularDetail?.isGone = true
        movieView?.isGone = false
        navigateToDetailScreen(currentMovieId)
    }

    private fun navigateToDetailScreen (movieId : Int?){
        var detailIntent = Intent(this , MovieDetailActivity::class.java)
        detailIntent?.putExtra(MOVIE_ID_kEY , movieId )
        startActivity(detailIntent)
    }

    private fun configureSimilarMovies() {
        similarMovieDiscreteView?.adapter = similarMoviesAdapter
    }

    private fun bindOurViewWithViewModel() {
        compositeDisposable?.add(detailViewModel?.getStatus()?.subscribe(::render))
        detailViewModel?.processIntents(collectOurIntents())
    }

    override fun render(state: DetailState) {

        if (state?.isLoadingForDetail == true) {

        }
        if (state?.isLoadingForSimilar == true) {

        }
        if (state?.errorDetail != null) {
            Toast.makeText(
                this,
                "Can`t load Movie Detail Because ${state?.errorDetail}",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (state?.errorSimilar != null) {
            Toast.makeText(
                this,
                "Can`t load Similar Moviea Because ${state?.errorDetail}",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (state?.movieDetail != null) {
            bindMovieDetailResponseToView(state?.movieDetail!!)
        }
        if (state?.similarMovies != null) {
            bindSimilarMoviesResponseToView(state?.similarMovies!!)
        }
    }

    override fun onBackPressed() {

    }

    private fun bindMovieDetailResponseToView (movieDetail : MovieDetailResponse){

        navigateBackFab?.setOnClickListener {
            this?.finish()
        }

        Picasso.get()?.load(imageApiBackground + movieDetail?.backdrop_path)?.transform(
            RoundedCornersTransformation(0 , 0)
        )?.into(movieDetailBackgroundImageView, object : Callback {
            override fun onSuccess() {
                detailBackgroundImageLoading?.makeItOff()
            }

            override fun onError(e: Exception?) {
                detailBackgroundImageLoading?.makeItOff()
            }

        })

        Picasso.get()?.load(imageApiPoster +movieDetail?.poster_path )?.transform(
            RoundedCornersTransformation(20 , 10)
        )?.into(movieDetailPosterImageView, object : Callback {
            override fun onSuccess() {
                detailPosterImageLoading?.makeItOff()
            }

            override fun onError(e: Exception?) {
                detailPosterImageLoading?.makeItOff()
            }

        })

        movieDetailTitle?.setText(movieDetail?.original_title)

        movieDetailCategoryTextView?.setText(movieDetail?.status)

        movieLanguageTextView?.setText(movieDetail?.original_language?.toUpperCase())

        movieDetailDescribtion?.setText(movieDetail?.overview)

        movieDetailWebsiteButton?.setOnClickListener {

            movieDetail?.homepage?.openWebsite(this)

        }
    }

    private fun bindSimilarMoviesResponseToView (movieDetail : MovieResponse){
        if(movieDetail?.results?.size == 0){
            emptySimilarContainer?.showVisibilty()
        }
        else {
            emptySimilarContainer?.hideVisibilty()
            similarMoviesAdapter?.submitNewMovies(movieDetail?.results as ArrayList<Result>)
        }
    }

    override fun collectOurIntents(): Observable<DetailIntent> {
        return Observable.merge(movieDetailPublishSubject, movieDetailSimilarPublishSubject)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getTransform(mStartView: View, mEndView: View , customDuration : Long): MaterialContainerTransform {
        return MaterialContainerTransform().apply {
            startView = mStartView
            endView = mEndView
            addTarget(mEndView)
            pathMotion = MaterialArcMotion()
            duration = customDuration
            scrimColor = Color.TRANSPARENT
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun animateCard(view: View) {
        movieView = view
        android.transition.TransitionManager.beginDelayedTransition(
            movieDetailRoot,
            getTransform(view, cardOfPopularDetail , 650)
        )
        view?.isGone = true
        cardOfPopularDetail?.isGone = false

    }

    private fun bindMovieDetailToPopularCard(movie: Result) {
        Picasso.get()?.load(imageApiPoster + movie?.poster_path)?.transform(
            RoundedCornersTransformation(20, 10)
        )?.into(popularImagePosterDetail, object : Callback {
            override fun onSuccess() {
                popularImageLoadingDetail?.makeItOff()
            }

            override fun onError(e: Exception?) {
                popularImageLoadingDetail?.makeItOff()
            }

        })
        popularCardNameDetail?.setText(movie?.title)
        popularCardDetailDetail?.setText(movie?.overview)

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPosition (duration : Long) {
        android.transition.TransitionManager.beginDelayedTransition(
            movieDetailRoot,
            getTransform(cardOfPopularDetail, movieView , duration)
        )
        cardOfPopularDetail?.isGone = true
        movieView?.isGone = false
    }

}