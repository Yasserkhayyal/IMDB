package com.morse.movie.ui.detail.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
import com.morse.movie.data.entity.personresponse.PersonResponse
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_more_movies.*
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.activity_movie_detail.detailPosterImageLoading
import kotlinx.android.synthetic.main.person_review_layout.*
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
    private var loadPersonProfileIntentSubject =
        PublishSubject.create<DetailIntent.LoadUserProfileIntent>()
    private lateinit var compositeDisposable: CompositeDisposable
    private var isExistInFavourite: Boolean? = null
    private val detailViewModel: DetailViewModel by lazy {
        val roomManager = RoomManager.invoke(this)
        val localSource = RoomClient(roomManager)
//        val localSource = RealmClient()
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
        val loadPersonProfile = LoadUserProfile(repository)
        val detailAnnotateProcessor = DetailAnnotateProcessor(
            loadMovieDetails,
            loadMovieSimilars,
            loadMovieReviews,
            loadMovieVideos,
            addMovieToDataBase,
            removeMovieFromFavourites,
            checkIfExistInDatabase,
            loadPersonProfile
        )
        val detailViewModelFactory = DetailViewModelFactory(detailAnnotateProcessor)
        ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel::class.java)
    }
    private var currentMovieId: Int? = null
    private lateinit var movieView: View
    private var youtubePlayerManager: YouTubePlayer? = null
    private var movieDetailResponse: MovieDetailResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        initializeYoutubeVideo()
        loadExtraData()
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        initAdapters()
        configureViewListeners()
        manageExtendedFabStatus()
        configureRecyclerViewsMoviesAdapters()
        bindOurViewWithViewModel()
        executeLoadAllData()
    }

    private fun initAdapters() {
        similarMoviesAdapter = MovieAdapter(RecyclerViewShape.VERTICAL, object : MovieListener {

            override fun onMovieClicks(movieCard: View, movieResult: Result, color: Int?) {

                currentMovieId = movieResult?.id!!
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (cardOfPopularDetail?.visibility == View.VISIBLE) {
                        returnCardToOriginPosition(
                            movieDetailRoot,
                            cardOfPopularDetail,
                            movieCard,
                            200
                        )
                        Observable.intervalRange(0, 100, 250, 0, TimeUnit.MILLISECONDS)
                            ?.subscribeOn(AndroidSchedulers.mainThread())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe {
                                animateCard(movieDetailRoot, cardOfPopularDetail, movieCard)
                                bindMovieDetailToPopularCard(movieResult)
                            }?.addTo(compositeDisposable)
                    } else {
                        animateCard(movieDetailRoot, cardOfPopularDetail, movieCard)
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


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        if (cardOfPersonDetail?.visibility == View.VISIBLE) {
                            returnCardToOriginPosition(
                                movieDetailRoot,
                                cardOfPersonDetail,
                                card!!,
                                200
                            )
                            Observable.intervalRange(0, 100, 250, 0, TimeUnit.MILLISECONDS)
                                ?.subscribeOn(AndroidSchedulers.mainThread())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe {
                                    animateCard(movieDetailRoot, cardOfPersonDetail, card)
                                    loadPersonProfileIntentSubject?.onNext(
                                        DetailIntent.LoadUserProfileIntent(
                                            data?.id!!
                                        )
                                    )
                                    // Call for Api
                                }?.addTo(compositeDisposable)
                        } else {
                            animateCard(movieDetailRoot, cardOfPersonDetail, card!!)
                            loadPersonProfileIntentSubject?.onNext(
                                DetailIntent.LoadUserProfileIntent(
                                    data?.id!!
                                )
                            )
                            // Call for Api
                        }
                    }


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
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        youtubePlayerManager?.loadVideo(data?.key!!, 0.0f)
                        if (cardOfYoutubeVideoPlayer?.visibility == View.VISIBLE) {
                            returnCardToOriginPosition(
                                movieDetailRoot,
                                cardOfYoutubeVideoPlayer,
                                card!!,
                                200
                            )
                            Observable.intervalRange(0, 100, 250, 0, TimeUnit.MILLISECONDS)
                                ?.subscribeOn(AndroidSchedulers.mainThread())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe {
                                    animateCard(movieDetailRoot, cardOfYoutubeVideoPlayer, card)
                                    loadPersonProfileIntentSubject?.onNext(
                                        DetailIntent.LoadUserProfileIntent(
                                            data?.id!!
                                        )
                                    )
                                    // Call for Api
                                }?.addTo(compositeDisposable)
                        } else {
                            animateCard(movieDetailRoot, cardOfYoutubeVideoPlayer, card!!)
                            // Call for Api
                        }
                    }

                    //data?.key?.openYoutubeVideo(this@MovieDetailActivity)
                }
            })

    }

    private fun initializeYoutubeVideo() {
        youtube_player_view?.initialize(object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {

            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {

            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {

            }

            override fun onPlaybackQualityChange(
                youTubePlayer: YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {

            }

            override fun onPlaybackRateChange(
                youTubePlayer: YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {
            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayerManager = youTubePlayer
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {

            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {

            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {

            }

            override fun onVideoLoadedFraction(
                youTubePlayer: YouTubePlayer,
                loadedFraction: Float
            ) {

            }
        })
    }

    private fun configureViewListeners() {
        navigateBackFab?.setOnClickListener {
            if (cardOfPopularDetail?.isGone == false) {
                returnCardToOriginPosition(movieDetailRoot, cardOfPopularDetail, movieView, 650)
            } else if (cardOfPersonDetail?.isGone == false) {
                returnCardToOriginPosition(movieDetailRoot, cardOfPersonDetail, movieView, 650)
            } else if (cardOfDeleteMovieFromFavourite?.isGone == false) {
                returnCardToOriginPosition(
                    movieDetailRoot,
                    cardOfDeleteMovieFromFavourite,
                    movieDetailFavouriteButton,
                    650
                )
            } else {
                finish()
            }
        }
        cardOfPopularDetail?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(movieDetailRoot, cardOfPopularDetail, movieView, 650)
            }
        }
        cardOfPersonDetail?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(movieDetailRoot, cardOfPersonDetail, movieView, 650)
            }
        }
        cardOfDeleteMovieFromFavourite?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(
                    movieDetailRoot,
                    cardOfDeleteMovieFromFavourite,
                    movieDetailFavouriteButton,
                    650
                )
            }
        }
        closeVideoFab?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                youtubePlayerManager?.pause()
                returnCardToOriginPosition(
                    movieDetailRoot,
                    cardOfYoutubeVideoPlayer,
                    movieView,
                    650
                )
            }
        }
        cardOfYoutubeVideoPlayer?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                youtubePlayerManager?.pause()
                returnCardToOriginPosition(
                    movieDetailRoot,
                    cardOfYoutubeVideoPlayer,
                    movieView,
                    650
                )
            }
        }
        cancelDeletButton?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(
                    movieDetailRoot,
                    cardOfDeleteMovieFromFavourite,
                    movieDetailFavouriteButton,
                    650
                )
            }
        }
        confirmDeletButton?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(
                    movieDetailRoot,
                    cardOfDeleteMovieFromFavourite,
                    movieDetailFavouriteButton,
                    650
                )
                makeFavouriteTheme()
                MovieCoordinator.openFlashbarSnackbar(
                    this,
                    " \n\n\n\n" +

                            movieDetailResponse?.original_title!! + "\n" +
                            "\n" ,
                    " ${movieDetailResponse?.title} has been Deleted successfully ," +
                            "you go to favourite screen to show all movies that you liked it",
                    R.drawable.fail_gradient ,
                    R.drawable.ic_broken_heart
                )

                removeMovieFromFavouriteIntentSubject?.onNext(
                    DetailIntent.RemoveMovieFromFavouriteIntent(
                        movieDetailResponse?.id!!
                    )
                )
                isExistInFavourite = false
            }
        }
        goToDetailOfMovieDetailDetailButton?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPositionWithNavigationAction(
                    movieDetailRoot,
                    cardOfPopularDetail,
                    movieView
                )
            }
        }
        movieDetailFavouriteButton?.setOnClickListener {
            if (isExistInFavourite == false) {
                makeUnfavouriteTheme()
                MovieCoordinator.openFlashbarSnackbar(
                    this,
                    " \n\n\n\n" +

                            movieDetailResponse?.original_title!! + "\n" +
                            "\n" ,
                     "${movieDetailResponse?.title} has been added successfully , you go to favourite screen to show all movies that you liked it",
                    R.drawable.success_gradient ,
                    R.drawable.ic_heart
                )

                addMovieToFavouriteIntentSubject?.onNext(
                    DetailIntent.AddMovieToFavouriteIntent(
                        movieDetailResponse!!
                    )
                )
                isExistInFavourite = true
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (cardOfDeleteMovieFromFavourite?.visibility == View.VISIBLE) {
                        deleteImageLoadingDetail?.showVisibilty()
                        movieDeletePoster?.loadImage(
                            movieDetailResponse?.poster_path,
                            movieDetailResponse?.original_title,
                            deleteImageLoadingDetail
                        )
                        returnCardToOriginPosition(
                            movieDetailRoot,
                            cardOfDeleteMovieFromFavourite,
                            movieDetailFavouriteButton!!,
                            200
                        )
                        Observable.intervalRange(0, 100, 250, 0, TimeUnit.MILLISECONDS)
                            ?.subscribeOn(AndroidSchedulers.mainThread())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe {
                                animateCard(
                                    movieDetailRoot,
                                    cardOfDeleteMovieFromFavourite,
                                    movieDetailFavouriteButton
                                )
                                //loadPersonProfileIntentSubject?.onNext(DetailIntent.LoadUserProfileIntent(data?.id!!))
                                // Call for Api
                            }?.addTo(compositeDisposable)
                    } else {
                        animateCard(
                            movieDetailRoot,
                            cardOfDeleteMovieFromFavourite,
                            movieDetailFavouriteButton!!
                        )
                        Glide.with(this)?.load(imageApiPoster + movieDetailResponse?.poster_path)
                            ?.circleCrop()
                            ?.into(movieDeletePoster)
                        //loadPersonProfileIntentSubject?.onNext(DetailIntent.LoadUserProfileIntent(data?.id!!))
                        // Call for Api
                    }
                }

            }
        }
    }

    private fun loadExtraData() {
        movieId = intent?.extras?.getInt(MOVIE_ID_kEY)
        movieDetailResponse = intent?.extras?.getParcelable(MOVIE_DETAIL_ID_kEY)
    }

    private fun manageExtendedFabStatus() {
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
    }

    private fun executeLoadAllData() {
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
        if (state?.isUserProfileLoading == true) {

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
        if (state?.userPorfileError != null) {
            Toast.makeText(
                this,
                "Can`t load Person Info Because ${state?.errorVideos}",
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
        if (state?.userProfile != null) {
            bindUserProfileResposneToView(state?.userProfile!!)
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
            returnCardToOriginPosition(movieDetailRoot, cardOfPopularDetail, movieView, 650)
        } else if (cardOfPersonDetail?.isGone == false) {
            returnCardToOriginPosition(movieDetailRoot, cardOfPersonDetail, movieView, 650)
        } else if (cardOfDeleteMovieFromFavourite?.isGone == false) {
            returnCardToOriginPosition(
                movieDetailRoot,
                cardOfDeleteMovieFromFavourite,
                movieDetailFavouriteButton,
                650
            )
        } else if (cardOfYoutubeVideoPlayer?.isGone == false) {
            returnCardToOriginPosition(movieDetailRoot, cardOfYoutubeVideoPlayer, movieView, 650)
            youtube_player_view?.release()
        } else {
            this?.finish()
        }
    }

    private fun bindMovieDetailResponseToView(movieDetail: MovieDetailResponse) {

        movieDetailBackgroundImageView?.loadImageAsBackground(
            movieDetail?.backdrop_path,
            movieDetail?.original_title,
            detailBackgroundImageLoading
        )

        movieDetailPosterImageView?.loadImage(
            movieDetail?.poster_path,
            movieDetail?.title,
            detailPosterImageLoading
        )

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

    private fun bindUserProfileResposneToView(personResponse: PersonResponse) {
        personImagePosterDetail?.loadImage(
            personResponse?.profilePath,
            personResponse?.name,
            personImageLoadingDetail
        )
        personCardNameDetail?.setText(personResponse?.name)
        personCardBioGrahyDetail?.setText(personResponse?.biography)
        personCardPlaceOfBearth?.setText(personResponse?.placeOfBirth)
        personCardDateOfBearth?.setText(personResponse?.birthday)
        if (personResponse?.gender == 2) {
            maleImageView?.setBackgroundResource(R.drawable.selected_gender)
        } else {
            femaleImageView?.setBackgroundResource(R.drawable.selected_gender)
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
            ?.mergeWith(movieVideosPublishSubject)
            ?.mergeWith(loadPersonProfileIntentSubject)!!
    }

    private fun bindMovieDetailToPopularCard(movie: Result) {
        popularImagePosterDetail?.loadImage(
            movie?.poster_path,
            movie?.title,
            popularImageLoadingDetail
        )
        popularCardNameDetail?.setText(movie?.title)
        popularCardDetailDetail?.setText(movie?.overview)

    }

    override fun onStop() {
        super.onStop()
        if (cardOfPopularDetail?.isGone == false) {
            returnCardToOriginPosition(movieDetailRoot, cardOfPopularDetail, movieView, 650)
        } else if (cardOfPersonDetail?.isGone == false) {
            returnCardToOriginPosition(movieDetailRoot, cardOfPersonDetail, movieView, 650)
        } else if (cardOfDeleteMovieFromFavourite?.isGone == false) {
            returnCardToOriginPosition(
                movieDetailRoot,
                cardOfDeleteMovieFromFavourite,
                movieDetailFavouriteButton,
                650
            )
        } else if (cardOfYoutubeVideoPlayer?.isGone == false) {
            returnCardToOriginPosition(movieDetailRoot, cardOfYoutubeVideoPlayer, movieView, 650)

        }
        compositeDisposable?.dispose()
        youtube_player_view?.release()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPositionWithNavigationAction(
        layoutRoot: ViewGroup,
        card: View,
        adapterView: View
    ) {
        android.transition.TransitionManager.beginDelayedTransition(
            layoutRoot,
            getTransform(card, movieView, 650)
        )
        card?.isGone = true
        adapterView?.isGone = false
        navigateToDetailScreen(currentMovieId)
    }

    // return a transformation already < DONE >
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

    // hide adapter item and show our Card Animation
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun animateCard(layoutRoot: ViewGroup, card: View, adapterView: View) {
        movieView = adapterView
        android.transition.TransitionManager.beginDelayedTransition(
            layoutRoot,
            getTransform(adapterView, card, 650)
        )
        adapterView?.isGone = true
        card?.isGone = false

    }

    // close card and show item of recyclerview
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPosition(
        layoutRoot: ViewGroup,
        card: View,
        adapterView: View,
        duration: Long
    ) {
        android.transition.TransitionManager.beginDelayedTransition(
            layoutRoot,
            getTransform(card, adapterView, duration)
        )
        card?.isGone = true
        adapterView?.isGone = false
    }

}