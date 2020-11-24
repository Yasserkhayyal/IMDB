package com.morse.movie.ui.home.activity

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
import com.jakewharton.rxbinding2.view.RxView
import com.morse.movie.R
import com.morse.movie.base.MviView
import com.morse.movie.remote.entity.movieresponse.Result
import com.morse.movie.ui.detail.activity.MovieDetailActivity
import com.morse.movie.ui.home.entities.HomeIntent
import com.morse.movie.ui.home.entities.HomeState
import com.morse.movie.ui.home.viewmodel.HomeViewModel
import com.morse.movie.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MviView<HomeIntent, HomeState> {

    private val homeViewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private var popularMovieIntentSubject = PublishSubject.create<HomeIntent.LoadPopularMovies>()
    private var topRatedMovieIntentSubject = PublishSubject.create<HomeIntent.LoadTopRatedMovies>()
    private var inComingMovieIntentSubject = PublishSubject.create<HomeIntent.LoadIncomingMovies>()
    private var nowPlayinggMovieIntentSubject =
        PublishSubject.create<HomeIntent.LoadNowPlayingMovies>()
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var popularMoviesAdapter: MovieAdapter
    lateinit var topRatedMoviesAdapter: MovieAdapter
    lateinit var inComingMoviesAdapter: MovieAdapter
    lateinit var nowPlayingMoviesAdapter: MovieAdapter
    var currentMovieId   : Int?  = 0
    private lateinit var movieView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        popularMoviesAdapter = MovieAdapter(object : MovieListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onMovieClicks(movieCard: View, movieResult: Result) {
                currentMovieId = movieResult?.id!!
                animateCard(movieCard)
                bindMovieDetailToPopularCard(movieResult)
            }
        })
        topRatedMoviesAdapter = MovieAdapter(object : MovieListener {
            override fun onMovieClicks(movieCard: View, movieResult: Result) {
                currentMovieId = movieResult?.id!!
                animateCard(movieCard)
                bindMovieDetailToPopularCard(movieResult)
            }
        })
        inComingMoviesAdapter = MovieAdapter(object : MovieListener {
            override fun onMovieClicks(movieCard: View, movieResult: Result) {
                currentMovieId = movieResult?.id!!
                animateCard(movieCard)
                bindMovieDetailToPopularCard(movieResult)
            }
        })
        nowPlayingMoviesAdapter = MovieAdapter(object : MovieListener {
            override fun onMovieClicks(movieCard: View, movieResult: Result) {
                currentMovieId = movieResult?.id!!
                animateCard(movieCard)
                bindMovieDetailToPopularCard(movieResult)
            }
        })
        bindViewModelWithView()
        configreAdapterToRecyclerview()
        configureClicksOnButtons()
        requestOnPopularMovies()
        requestOnTopRatedMovies()
        requestOnInComingMovies()
        requestOnNowPlayingMovies()
    }

    private fun configreAdapterToRecyclerview() {
        popularMoviesRecyclerView?.adapter = popularMoviesAdapter
        topRatedMoviesRecyclerView?.adapter = topRatedMoviesAdapter
        inComingMoviesRecyclerView?.adapter = inComingMoviesAdapter
        nowPlayingMoviesRecyclerView?.adapter = nowPlayingMoviesAdapter
    }

    override fun render(state: HomeState) {
        if (state?.isLoading == true) {
            if (state?.position == 0) {
                popularMoviesErrorContainer?.hideVisibilty()
                popularIndicatorView?.makeItOn()
            } else if (state?.position == 1) {
                topRatedMoviesErrorContainer?.hideVisibilty()
                topRatedIndicatorView?.makeItOn()
            } else if (state?.position == 2) {
                inComingMoviesErrorContainer?.hideVisibilty()
                inComingIndicatorView?.makeItOn()
            } else if (state?.position == 3) {
                nowPlayingMoviesErrorContainer?.hideVisibilty()
                nowPlayingIndicatorView?.makeItOn()
            }
        } else if (state?.error != null) {
            if (state?.position == 0) {
                popularMoviesErrorContainer?.showVisibilty()
                popularIndicatorView?.makeItOff()
                Toast.makeText(
                    this,
                    " Can`t Load Popular Movies Because ${state?.error}",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (state?.position == 1) {
                topRatedMoviesErrorContainer?.showVisibilty()
                topRatedIndicatorView?.makeItOff()
                Toast.makeText(
                    this,
                    " Can`t Load TopRated Movies Because ${state?.error}",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (state?.position == 2) {
                inComingMoviesErrorContainer?.showVisibilty()
                inComingIndicatorView?.makeItOff()
                Toast.makeText(
                    this,
                    " Can`t Load InComing Movies Because ${state?.error}",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (state?.position == 3) {
                nowPlayingMoviesErrorContainer?.showVisibilty()
                nowPlayingIndicatorView?.makeItOff()
                Toast.makeText(
                    this,
                    " Can`t Load NowPlaying Movies Because ${state?.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (state?.result != null) {
            if (state?.position == 0) {
                popularMoviesAdapter?.submitNewMovies(state?.result!!)
                popularMoviesErrorContainer?.hideVisibilty()
                popularIndicatorView?.makeItOff()
            } else if (state?.position == 1) {
                topRatedMoviesAdapter?.submitNewMovies(state?.result!!)
                topRatedMoviesErrorContainer?.hideVisibilty()
                topRatedIndicatorView?.makeItOff()
            } else if (state?.position == 2) {
                inComingMoviesAdapter?.submitNewMovies(state?.result!!)
                inComingMoviesErrorContainer?.hideVisibilty()
                inComingIndicatorView?.makeItOff()
            } else if (state?.position == 3) {
                nowPlayingMoviesAdapter?.submitNewMovies(state?.result!!)
                nowPlayingMoviesErrorContainer?.hideVisibilty()
                nowPlayingIndicatorView?.makeItOff()
            }

        }
    }

    private fun bindViewModelWithView() {
        compositeDisposable?.add(homeViewModel?.getStatus()?.subscribe(::render))
        homeViewModel?.processIntents(collectOurIntents())
    }

    override fun collectOurIntents(): Observable<HomeIntent> {
        return Observable.merge(
            popularMovieIntentSubject,
            topRatedMovieIntentSubject,
            inComingMovieIntentSubject,
            nowPlayinggMovieIntentSubject
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPosition () {
        android.transition.TransitionManager.beginDelayedTransition(
            homeScreenRoot,
            getTransform(cardOfPopular, movieView)
        )
        cardOfPopular?.isGone = true
        movieView?.isGone = false
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPositionWithNavigationAction () {
        android.transition.TransitionManager.beginDelayedTransition(
            homeScreenRoot,
            getTransform(cardOfPopular, movieView)
        )
        cardOfPopular?.isGone = true
        movieView?.isGone = false
        navigateToDetailScreen(currentMovieId)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun configureClicksOnButtons() {

        RxView.clicks(goToDetailOfMovieDetailButton)?.throttleLatest(500, TimeUnit.MILLISECONDS)
            ?.subscribe {
                returnCardToOriginPositionWithNavigationAction()

            }?.addTo(compositeDisposable)

        RxView.clicks(cardOfPopular)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {

            returnCardToOriginPosition()

        }?.addTo(compositeDisposable)

        RxView.clicks(retryPopularButton)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {
            popularMovieIntentSubject?.onNext(HomeIntent.LoadPopularMovies)
        }?.addTo(compositeDisposable)

        RxView.clicks(retryTopRatedButton)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {
            topRatedMovieIntentSubject?.onNext(HomeIntent.LoadTopRatedMovies)
        }?.addTo(compositeDisposable)

        RxView.clicks(retryInComingButton)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {
            inComingMovieIntentSubject?.onNext(HomeIntent.LoadIncomingMovies)
        }?.addTo(compositeDisposable)

        RxView.clicks(retryNowPlayingButton)?.throttleLatest(500, TimeUnit.MILLISECONDS)
            ?.subscribe {
                nowPlayinggMovieIntentSubject?.onNext(HomeIntent.LoadNowPlayingMovies)
            }?.addTo(compositeDisposable)

    }

    private fun navigateToDetailScreen (movieId : Int?){
        var detailIntent = Intent(this , MovieDetailActivity::class.java)
        detailIntent?.putExtra(MOVIE_ID_kEY , movieId )
        startActivity(detailIntent)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable?.dispose()
    }

    private fun requestOnPopularMovies() {
        if (popularMoviesAdapter?.itemCount == 0) {
            popularMovieIntentSubject?.onNext(HomeIntent.LoadPopularMovies)
        }
    }

    private fun requestOnTopRatedMovies() {
        if (topRatedMoviesAdapter?.itemCount == 0) {
            topRatedMovieIntentSubject?.onNext(HomeIntent.LoadTopRatedMovies)
        }
    }

    private fun requestOnInComingMovies() {
        if (inComingMoviesAdapter?.itemCount == 0) {
            inComingMovieIntentSubject?.onNext(HomeIntent.LoadIncomingMovies)
        }
    }

    private fun requestOnNowPlayingMovies() {
        if (nowPlayingMoviesAdapter?.itemCount == 0) {
            nowPlayinggMovieIntentSubject?.onNext(HomeIntent.LoadNowPlayingMovies)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getTransform(mStartView: View, mEndView: View): MaterialContainerTransform {
        return MaterialContainerTransform().apply {
            startView = mStartView
            endView = mEndView
            addTarget(mEndView)
            pathMotion = MaterialArcMotion()
            duration = 850
            scrimColor = Color.TRANSPARENT
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun animateCard(view: View) {
        movieView = view
        android.transition.TransitionManager.beginDelayedTransition(
            homeScreenRoot,
            getTransform(view, cardOfPopular)
        )
        view?.isGone = true
        cardOfPopular?.isGone = false

    }

    private fun bindMovieDetailToPopularCard(movie: Result) {
        Picasso.get()?.load(imageApiPoster + movie?.poster_path)?.transform(
            RoundedCornersTransformation(20, 10)
        )?.into(popularImagePoster, object : Callback {
            override fun onSuccess() {
                popularImageLoading?.makeItOff()
            }

            override fun onError(e: Exception?) {
                popularImageLoading?.makeItOff()
            }

        })
        popularCardName?.setText(movie?.title)
        popularCardDetail?.setText(movie?.overview)

    }

}
