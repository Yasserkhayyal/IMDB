package com.morse.movie.ui.more.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.jakewharton.rxbinding2.view.RxView
import com.morse.movie.R
import com.morse.movie.base.MviView
import com.morse.movie.remote.datasource.manager.MoreDataSourceManager
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.repository.DataRepositoryImpl
import com.morse.movie.domain.usecase.LoadPaginationMovies
import com.morse.movie.domain.usecase.SearchOnMoviesUseCase
import com.morse.movie.local.manager.RoomClient
import com.morse.movie.local.room_core.RoomManager
import com.morse.movie.remote.manager.RetrofitClient
import com.morse.movie.ui.detail.activity.MovieDetailActivity
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.ui.more.entities.MoreIntent
import com.morse.movie.ui.more.entities.MoreState
import com.morse.movie.ui.more.viewmodel.MoreAnnotateProcessor
import com.morse.movie.ui.more.viewmodel.MoreViewModel
import com.morse.movie.ui.more.viewmodel.MoreViewModelFactory
import com.morse.movie.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_more_movies.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MoreMoviesActivity : AppCompatActivity(), MviView<MoreIntent, MoreState>,
    SearchView.OnQueryTextListener {

    private var movieView: View? = null
    private var currentMovieId: Int? = 0
    private var moviePosition: Int? = null
    private val moreViewMode: MoreViewModel by lazy {

        val roomManager = RoomManager.invoke(this)
        val localSource = RoomClient(roomManager)
        val remoteSource = RetrofitClient()
        val repository = DataRepositoryImpl (remoteSource , localSource)
        val loadPaginationMovies = LoadPaginationMovies(repository)
        val searchPaginationMovies = SearchOnMoviesUseCase(repository)
        val moreAnnotateProcressor = MoreAnnotateProcessor(loadPaginationMovies , searchPaginationMovies)
        val moreViewModelFactory = MoreViewModelFactory(moreAnnotateProcressor)
        ViewModelProviders.of(this, moreViewModelFactory).get(MoreViewModel::class.java)
    }
    private lateinit var moreMovieAdapter: MoreMovieAdapter
    private var popularIntentPublishSubject: PublishSubject<MoreIntent.LoadPopularMoviesByPagination> = PublishSubject.create()
    private var topRatedIntentPublishSubject: PublishSubject<MoreIntent.LoadTopRatedMoviesByPagination> = PublishSubject.create()
    private var inComingIntentPublishSubject: PublishSubject<MoreIntent.LoadInComingMoviesByPagination> = PublishSubject.create()
    private var nowPlayingIntentPublishSubject: PublishSubject<MoreIntent.LoadNowPlayingMoviesByPagination> = PublishSubject.create()
    private var searchIntentPublishSubject: PublishSubject<MoreIntent.SearchOnMoviesByPagination> = PublishSubject.create()
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_movies)
        setSupportActionBar(moreActivityActionBar)
        moviePosition = intent?.extras?.getInt(MOVIE_TYPE)
        chooseCorrectTitle(moviePosition)
        moreActivityActionBar?.setNavigationOnClickListener {
            if (cardOfMore?.isGone == false) {
                returnCardToOriginPosition(650)
            } else {
                this?.finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        moreMovieAdapter = MoreMovieAdapter(object : MovieListener {
            override fun onMovieClicks(movieCard: View, movieResult: Result, color: Int?) {
                currentMovieId = movieResult?.id!!
                if (cardOfMore?.visibility == View.VISIBLE) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        returnCardToOriginPosition(200)
                    }
                    Observable.intervalRange(0, 100, 300, 0, TimeUnit.MILLISECONDS)
                        ?.subscribeOn(AndroidSchedulers.mainThread())
                        ?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                animateCard(movieCard)
                            }
                            bindMovieDetailToPopularCard(movieResult, color)
                        }?.addTo(compositeDisposable)
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        animateCard(movieCard)
                    }
                    bindMovieDetailToPopularCard(movieResult, color)
                }
            }
        })
        compositeDisposable = CompositeDisposable()
        configureReceyclerViewWithAdapter()
        bindViewModerWithOurView()
        RxView.clicks(cardOfMore)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(650)
            }

        }?.addTo(compositeDisposable)
        RxView.clicks(goToDetailOfMovieDetailMoreButton)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    returnCardToOriginPositionWithNavigationAction()
                }

            }?.addTo(compositeDisposable)
        moreMoviesLoading?.makeItOn()
        callOnPagination()
    }

    private fun callOnPagination () {
        if (moviePosition == 0) {
            popularIntentPublishSubject?.onNext(MoreIntent.LoadPopularMoviesByPagination)
        }
        else if (moviePosition == 1) {
            topRatedIntentPublishSubject?.onNext(MoreIntent.LoadTopRatedMoviesByPagination)
        }
        else if (moviePosition == 2) {
            inComingIntentPublishSubject?.onNext(MoreIntent.LoadInComingMoviesByPagination)
        }
        else if (moviePosition == 3) {
            nowPlayingIntentPublishSubject?.onNext(MoreIntent.LoadNowPlayingMoviesByPagination)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPosition(duration: Long) {
        android.transition.TransitionManager.beginDelayedTransition(
            moreScreenRoot,
            getTransform(cardOfMore, movieView!!, duration)
        )
        cardOfMore?.isGone = true
        movieView?.isGone = false
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPositionWithNavigationAction() {
        android.transition.TransitionManager.beginDelayedTransition(
            moreScreenRoot,
            getTransform(cardOfMore, movieView!!, 650)
        )
        cardOfMore?.isGone = true
        movieView?.isGone = false
        navigateToDetailScreen(currentMovieId)
    }

    private fun navigateToDetailScreen(movieId: Int?) {
        var detailIntent = Intent(this, MovieDetailActivity::class.java)
        detailIntent?.putExtra(MOVIE_ID_kEY, movieId)
        startActivity(detailIntent)
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

    private fun bindMovieDetailToPopularCard(movie: Result, color: Int?) {
        Picasso.get()?.load(imageApiPoster + movie?.poster_path)?.transform(
            RoundedCornersTransformation(20, 10)
        )?.into(moreImagePoster, object : Callback {
            override fun onSuccess() {
                moreImageLoading?.makeItOff()
            }

            override fun onError(e: Exception?) {
                moreImageLoading?.makeItOff()
            }

        })
        cardOfMore?.setCardBackgroundColor(color!!)
        moreCardName?.setText(movie?.title)
        moreCardDetail?.setText(movie?.overview)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun animateCard(view: View) {
        movieView = view
        android.transition.TransitionManager.beginDelayedTransition(
            moreScreenRoot,
            getTransform(view, cardOfMore, 650)
        )
        view?.isGone = true
        cardOfMore?.isGone = false

    }

    private fun bindViewModerWithOurView() {
        compositeDisposable?.add(moreViewMode?.getStatus()?.subscribe(::render))
        moreViewMode?.processIntents(collectOurIntents())
    }

    private fun configureReceyclerViewWithAdapter() {
        moreMoviesRecyclerView?.adapter = moreMovieAdapter
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable?.dispose()
    }

    private fun chooseCorrectTitle(position: Int?) {
        if (position == 0) {
            moreActivityActionBar?.title = "Popular Movies"
        } else if (position == 1) {
            moreActivityActionBar?.title = "Top Rated Movies"
        } else if (position == 2) {
            moreActivityActionBar?.title = "Incoming Movies"
        } else if (position == 3) {
            moreActivityActionBar?.title = "Now Playing Movies"
        }
    }

    override fun onBackPressed() {
        if (cardOfMore?.isGone == false) {
            returnCardToOriginPosition(650)
        } else {
            this?.finish()
        }
    }

    override fun render(state: MoreState) {
        if (state?.isLoading == true) {
            moreMoviesLoading?.makeItOn()
            moreMoviesErrorContainer?.hideVisibilty()
        } else if (state?.error != null) {
            moreMoviesLoading?.makeItOff()
            moreMoviesErrorContainer?.showVisibilty()
        } else if (state?.data != null) {
            moreMoviesLoading?.makeItOff()
            moreMoviesErrorContainer?.hideVisibilty()
            state?.data?.observe(this, {
                moreMovieAdapter?.submitList(it)
            }

            )
        }
    }

    override fun collectOurIntents(): Observable<MoreIntent> {
        return Observable.merge(
            popularIntentPublishSubject,
            topRatedIntentPublishSubject,
            inComingIntentPublishSubject,
            nowPlayingIntentPublishSubject
        )?.mergeWith(searchIntentPublishSubject)!!
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        // Associate searchable configuration with the SearchView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            (menu.findItem(R.id.search).actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                this?.setOnQueryTextListener(this@MoreMoviesActivity)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                onSearchRequested()
                true
            }
            else -> false
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
       // searchIntentPublishSubject?.onNext(MoreIntent.SearchOnMoviesByPagination(query!!))
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText?.isNotEmpty()!!) {
            moreMoviesRecyclerView?.hideVisibilty()
            moreMoviesErrorContainer?.hideVisibilty()
            moreMoviesLoading?.makeItOn()
            MoreDataSourceManager()?.getPaginationMoviesSearch(newText!!)?.observe(this, {
                moreMovieAdapter?.submitList(it)
                moreMoviesLoading?.makeItOff()
                moreMoviesRecyclerView?.showVisibilty()
            })
        }
        else{
            callOnPagination()
        }
       // searchIntentPublishSubject?.onNext(MoreIntent.SearchOnMoviesByPagination(newText!!))
        return true
    }

}