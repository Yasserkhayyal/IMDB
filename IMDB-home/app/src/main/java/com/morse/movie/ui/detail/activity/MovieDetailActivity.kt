package com.morse.movie.ui.detail.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.morse.movie.R
import com.morse.movie.app.coordinator.MovieCoordinator
import com.morse.movie.base.MviView
import com.morse.movie.base.RecyclerViewShape
import com.morse.movie.base.RecyclerviewListener
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.MovieResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.entity.moviereviewresponse.MovieReview
import com.morse.movie.data.entity.movievideosresponse.MovieVideoResponse
import com.morse.movie.data.repository.DataRepositoryImpl
import com.morse.movie.domain.usecase.*
import com.morse.movie.local.realm_core.RealmClient
import com.morse.movie.local.room_core.RoomClient
import com.morse.movie.local.room_core.RoomManager
import com.morse.movie.remote.retrofit_core.core.RetrofitClient
import com.morse.movie.remote.retrofit_core.datasource.manager.RetrofitMoreDataSourceManager
import com.morse.movie.ui.detail.adapter.ReviewAdapter
import com.morse.movie.ui.detail.adapter.VideoAdapter
import com.morse.movie.ui.detail.entities.DetailIntent
import com.morse.movie.ui.detail.entities.DetailState
import com.morse.movie.ui.detail.viewmodel.DetailAnnotateProcessor
import com.morse.movie.ui.detail.viewmodel.DetailViewModel
import com.morse.movie.ui.detail.viewmodel.DetailViewModelFactory
import com.morse.movie.ui.home.activity.MovieAdapter
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_more_movies.*
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MovieDetailActivity : AppCompatActivity(), MviView<DetailIntent, DetailState> {

    private lateinit var similarMoviesAdapter: MovieAdapter
    private lateinit var reviewMoviesAdapter: ReviewAdapter
    private lateinit var videoMoviesAdapter: VideoAdapter
    private var movieId: Int? = 0
    private var movieDetailPublishSubject: PublishSubject<DetailIntent.LoadMovieDetails> =
        PublishSubject.create()
    private var movieDetailSimilarPublishSubject: PublishSubject<DetailIntent.LoadSimilarMovies> =
        PublishSubject.create()
    private var addMovieToFavouriteIntentSubject =
        PublishSubject.create<DetailIntent.AddMovieToFavouriteIntent>()

    private var movieReviewsPublishSubject: PublishSubject<DetailIntent.LoadMovieReviewsIntent> =
        PublishSubject.create()
    private var movieVideosPublishSubject =
        PublishSubject.create<DetailIntent.LoadMovieVideosIntent>()

    private var removeMovieFromFavouriteIntentSubject =
        PublishSubject.create<DetailIntent.RemoveMovieFromFavouriteIntent>()
    private var isMovieExistInFavouriteIntentSubject =
        PublishSubject.create<DetailIntent.IsMovieExistInDatabaseIntent>()
    private lateinit var compositeDisposable: CompositeDisposable
    private var isExistInFavourite: Boolean? = null
    private val detailViewModel: DetailViewModel by lazy {
//        val roomManager = RoomManager.invoke(this)
//        val localSource = RoomClient(roomManager)
        val localSource = RealmClient()
        val dataManager = RetrofitMoreDataSourceManager()
        val remoteSource = RetrofitClient(dataManager)
//        val dataManager = FuelMoreDataSourceManager()
//        val remoteSource = FuelClient(dataManager)
        val repository = DataRepositoryImpl(remoteSource, localSource)
        val loadMovieDetails = LoadMovieDetails(repository)
        val loadMovieSimilars = LoadSimilarMovies(repository)
        val loadMovieReviews = LoadMovieReviews(repository)
        val loadMovieVideos = LoadMovieVideos(repository)
        val checkIfExistInDatabase = CheckIfMovieExistInDataBase(repository)
        val addMovieToDataBase = AddMovieToFavourites(repository)
        val removeMovieFromFavourites = RemoveMovieFromFavourites(repository)
        val detailAnnotateProcessor = DetailAnnotateProcessor(
            loadMovieDetails,
            loadMovieSimilars,
            loadMovieReviews,
            loadMovieVideos,
            addMovieToDataBase,
            removeMovieFromFavourites,
            checkIfExistInDatabase
        )
        val detailViewModelFactory = DetailViewModelFactory(detailAnnotateProcessor)
        ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel::class.java)
    }
    private var currentMovieId: Int? = null
    private lateinit var movieView: View
    private var movieDetailResponse: MovieDetailResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        movieId = intent?.extras?.getInt(MOVIE_ID_kEY)
        movieDetailResponse = intent?.extras?.getParcelable(MOVIE_DETAIL_ID_kEY)
    }

    override fun onStart() {
        super.onStart()
        similarMoviesAdapter = MovieAdapter(RecyclerViewShape.VERTICAL, object : MovieListener {

            override fun onMovieClicks(movieCard: View, movieResult: Result, color: Int?) {

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
        reviewMoviesAdapter = ReviewAdapter(
            RecyclerViewShape.VERTICAL,
            object : RecyclerviewListener<com.morse.movie.data.entity.moviereviewresponse.Result> {

                override fun onItemPressed(
                    card: View?,
                    data: com.morse.movie.data.entity.moviereviewresponse.Result?,
                    color: Int?
                ) {
                }
            })
        videoMoviesAdapter = VideoAdapter(
            RecyclerViewShape.VERTICAL,
            object : RecyclerviewListener<com.morse.movie.data.entity.movievideosresponse.Result> {

                override fun onItemPressed(
                    card: View?,
                    data: com.morse.movie.data.entity.movievideosresponse.Result?,
                    color: Int?
                ) {
                    data?.key?.openYoutubeVideo(this@MovieDetailActivity)
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
        movieDetailFavouriteButton?.setOnClickListener {
            if (isExistInFavourite == false) {
                makeUnfavouriteTheme()
                MovieCoordinator.openAddedSnackbar(
                    movieDetailRoot,
                    movieDetailResponse?.original_title
                )
                addMovieToFavouriteIntentSubject?.onNext(
                    DetailIntent.AddMovieToFavouriteIntent(
                        movieDetailResponse!!
                    )
                )
                isExistInFavourite = true
            } else {
                makeFavouriteTheme()
                MovieCoordinator.openDeleteSnackbar(
                    movieDetailRoot,
                    movieDetailResponse?.original_title
                )
                removeMovieFromFavouriteIntentSubject?.onNext(
                    DetailIntent.RemoveMovieFromFavouriteIntent(
                        movieDetailResponse?.id!!
                    )
                )
                isExistInFavourite = false
            }
        }
        compositeDisposable = CompositeDisposable()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            movieDetailScrollView?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > oldScrollY) {
                    movieDetailWebsiteButton?.shrink()
                    movieDetailFavouriteButton?.shrink()
                } else {
                    movieDetailWebsiteButton?.shrink()
                    movieDetailFavouriteButton?.shrink()
                }
            }
        }
        configureRecyclerViewsMoviesAdapters()
        bindOurViewWithViewModel()
        isMovieExistInFavouriteIntentSubject?.onNext(
            DetailIntent.IsMovieExistInDatabaseIntent(
                movieId!!
            )
        )
        movieDetailPublishSubject?.onNext(DetailIntent.LoadMovieDetails(movieId!!))
        movieDetailSimilarPublishSubject?.onNext(DetailIntent.LoadSimilarMovies(movieId!!))

        movieReviewsPublishSubject?.onNext(DetailIntent.LoadMovieReviewsIntent(movieId!!))
        movieVideosPublishSubject?.onNext(DetailIntent.LoadMovieVideosIntent(movieId!!))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPositionWithNavigationAction() {
        android.transition.TransitionManager.beginDelayedTransition(
            movieDetailRoot,
            getTransform(cardOfPopularDetail, movieView, 650)
        )
        cardOfPopularDetail?.isGone = true
        movieView?.isGone = false
        navigateToDetailScreen(currentMovieId)
    }

    private fun navigateToDetailScreen(movieId: Int?) {
        var detailIntent = Intent(this, MovieDetailActivity::class.java)
        detailIntent?.putExtra(MOVIE_ID_kEY, movieId)
        startActivity(detailIntent)
    }

    private fun configureRecyclerViewsMoviesAdapters() {
        similarMovieDiscreteView?.adapter = similarMoviesAdapter
        peopleReviewMovieDiscreteView?.adapter = reviewMoviesAdapter
        videosDiscreteView.scrollToPosition(2)
        videosDiscreteView.setSlideOnFling(true)
        videosDiscreteView?.setItemTransformer(

            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.5f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build()

        )
        videosDiscreteView?.setSlideOnFling(true)
        videosDiscreteView?.adapter = videoMoviesAdapter
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
        if (state?.isLoadingReviews == true) {

        }
        if (state?.isLoadingVideos == true) {

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
        if (state?.errorReviews != null) {
            Toast.makeText(
                this,
                "Can`t load Movie Reviews Because ${state?.errorReviews}",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (state?.errorVideos != null) {
            Toast.makeText(
                this,
                "Can`t load Movie Videos Because ${state?.errorVideos}",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (state?.isAdded != null) {

        }
        if (state?.isRemoved != null) {
        }
        if (state?.movieDetail != null) {
            movieDetailResponse = state?.movieDetail
            bindMovieDetailResponseToView(state?.movieDetail!!)
        }
        if (state?.similarMovies != null) {
            bindSimilarMoviesResponseToView(state?.similarMovies!!)
        }
        if (state?.isExist != null) {
            isExistInFavourite = state?.isExist
            if (state?.isExist == true) {
                makeUnfavouriteTheme()
            } else {
                makeFavouriteTheme()
            }
            movieDetailWebsiteButton?.setText("View Website")
        }
        if (state?.movieReviews != null) {
            bindMovieReviewResponseToView(state?.movieReviews!!)
        }
        if (state?.movieVideos != null) {
            bindMovieVideosResponseToView(state?.movieVideos!!)
        }
    }

    private fun makeFavouriteTheme() {
        runOnUiThread {
            movieDetailFavouriteButton?.setText("Favourite it")
            movieDetailFavouriteButton?.setBackgroundColor(resources?.getColor(R.color.colorOfRed)!!)
            Log.i("mooooooooomomoo", "make it Favourite")
        }
    }

    private fun makeUnfavouriteTheme() {
        runOnUiThread {
            movieDetailFavouriteButton?.setText("UnFavourite it")
            movieDetailFavouriteButton?.setBackgroundColor(resources?.getColor(R.color.colorOfAmber)!!)
            Log.i("mooooooooomomoo", "make it UnFavourite")
        }
    }

    override fun onBackPressed() {
        if (cardOfPopularDetail?.isGone == false) {
            returnCardToOriginPosition(650)
        } else {
            this?.finish()
        }
    }

    private fun bindMovieDetailResponseToView(movieDetail: MovieDetailResponse) {

        navigateBackFab?.setOnClickListener {
            if (cardOfPopularDetail?.isGone == false) {
                returnCardToOriginPosition(650)
            } else {
                this?.finish()
            }
        }

        Picasso.get()?.load(imageApiBackground + movieDetail?.backdrop_path)?.transform(
            RoundedCornersTransformation(0, 0)
        )?.into(movieDetailBackgroundImageView, object : Callback {
            override fun onSuccess() {
                detailBackgroundImageLoading?.makeItOff()
            }

            override fun onError(e: Exception?) {
                detailBackgroundImageLoading?.makeItOff()
            }

        })

        Picasso.get()?.load(imageApiPoster + movieDetail?.poster_path)?.transform(
            RoundedCornersTransformation(20, 10)
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

    private fun bindSimilarMoviesResponseToView(movieDetail: MovieResponse) {
        if (movieDetail?.results?.size == 0) {
            similerTitleHeaderTextView?.hideVisibilty()
            emptySimilarContainer?.hideVisibilty()
        } else {
            similerTitleHeaderTextView?.showVisibilty()
            emptySimilarContainer?.hideVisibilty()
            similarMoviesAdapter?.submitNewMovies(movieDetail?.results as ArrayList<Result>)
        }
    }

    private fun bindMovieVideosResponseToView(movieDetail: MovieVideoResponse) {
        if (movieDetail?.results?.size == 0) {
            videosTitleHeaderTextView?.hideVisibilty()
            emptyVideosContainer?.hideVisibilty()
        } else {
            videosTitleHeaderTextView?.showVisibilty()
            emptyVideosContainer?.hideVisibilty()
            videoMoviesAdapter?.submitNewVideos(movieDetail?.results as ArrayList<com.morse.movie.data.entity.movievideosresponse.Result>)
        }
    }

    private fun bindMovieReviewResponseToView(movieDetail: MovieReview) {
        if (movieDetail?.results?.size == 0) {
            reviewTitleHeaderTextView?.hideVisibilty()
            emptyReviewContainer?.hideVisibilty()
        } else {
            reviewTitleHeaderTextView?.showVisibilty()
            emptyReviewContainer?.hideVisibilty()
            reviewMoviesAdapter?.submitNewReviews(movieDetail?.results as ArrayList<com.morse.movie.data.entity.moviereviewresponse.Result>)
        }
    }

    override fun collectOurIntents(): Observable<DetailIntent> {
        return Observable.merge(
            addMovieToFavouriteIntentSubject,
            removeMovieFromFavouriteIntentSubject,
            movieDetailPublishSubject,
            movieDetailSimilarPublishSubject
        )?.mergeWith(isMovieExistInFavouriteIntentSubject)
            ?.mergeWith(movieReviewsPublishSubject)
            ?.mergeWith(movieVideosPublishSubject)!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getTransform(
        mStartView: View,
        mEndView: View,
        customDuration: Long
    ): MaterialContainerTransform {
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
            getTransform(view, cardOfPopularDetail, 650)
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
    private fun returnCardToOriginPosition(duration: Long) {
        android.transition.TransitionManager.beginDelayedTransition(
            movieDetailRoot,
            getTransform(cardOfPopularDetail, movieView, duration)
        )
        cardOfPopularDetail?.isGone = true
        movieView?.isGone = false
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable?.dispose()
    }

}