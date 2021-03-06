package com.morse.movie.ui.home.activity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.morse.movie.R
import com.morse.movie.app.coordinator.MovieCoordinator
import com.morse.movie.base.MviView
import com.morse.movie.base.RecyclerViewShape
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.repository.DataRepositoryImpl
import com.morse.movie.domain.usecase.LoadInComingMovies
import com.morse.movie.domain.usecase.LoadNowPlayingMovies
import com.morse.movie.domain.usecase.LoadPopularMovies
import com.morse.movie.domain.usecase.LoadTopRatedMovies
import com.morse.movie.remote.retrofit_core.core.RetrofitClient
import com.morse.movie.remote.retrofit_core.datasource.manager.RetrofitMoreDataSourceManager
import com.morse.movie.ui.home.entities.HomeIntent
import com.morse.movie.ui.home.entities.HomeState
import com.morse.movie.ui.home.viewmodel.HomeAnnotateProcessor
import com.morse.movie.ui.home.viewmodel.HomeViewModel
import com.morse.movie.ui.home.viewmodel.HomeViewModelFactory
import com.morse.movie.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MviView<HomeIntent, HomeState> {

    private val homeViewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
//        val roomManager = RoomManager.invoke(this)
//        val localSource = RoomClient(roomManager)
//        val localSource = RealmClient()
//        val cacheManager = CacheManager.newInstance()
        val dataManager = RetrofitMoreDataSourceManager()
        val remoteSource = RetrofitClient(dataManager)
//        val dataManager = FuelMoreDataSourceManager()
//        val remoteSource = FuelClient(dataManager)
        val repository = DataRepositoryImpl ( remoteInterface = remoteSource )
        val loadPopularMovies = LoadPopularMovies(repository)
        val loadTopRatedMovies = LoadTopRatedMovies(repository)
        val loadInComingMovies = LoadInComingMovies(repository)
        val loadNoPlayingMovies = LoadNowPlayingMovies(repository)
        val homeAnnotateProcessor = HomeAnnotateProcessor(
            loadPopularMovies,
            loadTopRatedMovies,
            loadInComingMovies,
            loadNoPlayingMovies
        )
        val homeViewModelFactory =
            WeakReference<HomeViewModelFactory>(HomeViewModelFactory(homeAnnotateProcessor))
        ViewModelProviders.of(this, homeViewModelFactory?.get()!!).get(HomeViewModel::class.java)
    }
    private var popularMovieIntentSubject = PublishSubject.create<HomeIntent.LoadPopularMovies>()
    private var topRatedMovieIntentSubject = PublishSubject.create<HomeIntent.LoadTopRatedMovies>()
    private var inComingMovieIntentSubject = PublishSubject.create<HomeIntent.LoadIncomingMovies>()
    private var nowPlayinggMovieIntentSubject = PublishSubject.create<HomeIntent.LoadNowPlayingMovies>()

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var popularMoviesAdapter: MovieAdapter
    lateinit var topRatedMoviesAdapter: MovieAdapter
    lateinit var inComingMoviesAdapter: MovieAdapter
    lateinit var nowPlayingMoviesAdapter: MovieAdapter
    var currentMovieId: Int? = 0
    private lateinit var movieView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        initAdapters()
        manageFabAnimation ()
        bindViewModelWithView()
        configreAdapterToRecyclerview()
        configureClicksOnButtons()
        requestOnPopularMovies()
        requestOnTopRatedMovies()
        requestOnInComingMovies()
        requestOnNowPlayingMovies()
    }

    private fun manageFabAnimation (){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            homeNestedScrollView?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > oldScrollY) {
                    goToFavouriteButton?.shrink()
                } else {
                    goToFavouriteButton?.extend()
                }
            }
        }
    }

    private fun initAdapters (){
        popularMoviesAdapter = MovieAdapter(RecyclerViewShape.VERTICAL, object : MovieListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onMovieClicks(movieCard: View, movieResult: Result, color: Int?) {
                currentMovieId = movieResult?.id!!
                if (cardOfPopular?.visibility == View.VISIBLE) {
                    returnCardToOriginPosition(200)
                    Observable.intervalRange(0, 100, 300, 0, TimeUnit.MILLISECONDS)
                        ?.subscribeOn(AndroidSchedulers.mainThread())
                        ?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                            animateCard(movieCard)
                            bindMovieDetailToPopularCard(movieResult)
                        }?.addTo(compositeDisposable)
                } else {
                    animateCard(movieCard)
                    bindMovieDetailToPopularCard(movieResult)
                }
            }
        })
        topRatedMoviesAdapter = MovieAdapter(RecyclerViewShape.VERTICAL, object : MovieListener {
            override fun onMovieClicks(movieCard: View, movieResult: Result, color: Int?) {
                currentMovieId = movieResult?.id!!
                if (cardOfPopular?.visibility == View.VISIBLE) {
                    returnCardToOriginPosition(200)
                    Observable.intervalRange(0, 100, 300, 0, TimeUnit.MILLISECONDS)
                        ?.subscribeOn(AndroidSchedulers.mainThread())
                        ?.observeOn(AndroidSchedulers.mainThread())?.subscribe {
                            animateCard(movieCard)
                            bindMovieDetailToPopularCard(movieResult)
                        }?.addTo(compositeDisposable)
                } else {
                    animateCard(movieCard)
                    bindMovieDetailToPopularCard(movieResult)
                }
            }
        })
        inComingMoviesAdapter = MovieAdapter(RecyclerViewShape.VERTICAL, object : MovieListener {
            override fun onMovieClicks(movieCard: View, movieResult: Result, color: Int?) {
                currentMovieId = movieResult?.id!!
                if (cardOfPopular?.visibility == View.VISIBLE) {
                    returnCardToOriginPosition(200)
                    Observable.intervalRange(0, 100, 300, 0, TimeUnit.MILLISECONDS)
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
        })
        nowPlayingMoviesAdapter = MovieAdapter(RecyclerViewShape.VERTICAL, object : MovieListener {
            override fun onMovieClicks(movieCard: View, movieResult: Result, color: Int?) {
                currentMovieId = movieResult?.id!!
                if (cardOfPopular?.visibility == View.VISIBLE) {
                    returnCardToOriginPosition(200)
                    Observable.intervalRange(0, 100, 300, 0, TimeUnit.MILLISECONDS)
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
        })
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
    private fun returnCardToOriginPosition(duration: Long) {
        android.transition.TransitionManager.beginDelayedTransition(
            homeScreenRoot,
            getTransform(cardOfPopular, movieView, duration)
        )
        cardOfPopular?.isGone = true
        movieView?.isGone = false
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPositionOfSetting(duration: Long) {
        android.transition.TransitionManager.beginDelayedTransition(
            homeScreenRoot,
            getTransform(cardOfSetting, settingLayout, duration)
        )
        cardOfSetting?.isGone = true
        settingLayout?.isGone = false
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPositionOfProfile(duration: Long) {

        android.transition.TransitionManager.beginDelayedTransition(
            homeScreenRoot,
            getTransform(meCard, myProfile, duration)
        )
        meCard?.isGone = true
        myProfile?.isGone = false

    }

    private fun returnCardToOriginPositionWithNavigationAction() {
        android.transition.TransitionManager.beginDelayedTransition(
            homeScreenRoot,
            getTransform(cardOfPopular, movieView, 650)
        )
        cardOfPopular?.isGone = true
        movieView?.isGone = false
        navigateToDetailScreen(currentMovieId)
    }

    private fun manageDarkMode (isEnabled : Boolean){
        if( Build.VERSION.SDK_INT >=  Build.VERSION_CODES.Q) {
            if (isEnabled == false) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        else {
            Toast.makeText(this , "Unfortinatlly , Your Phone didn`t support dark mode . " , Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun configureClicksOnButtons() {
        RxView.clicks(settingLayout)?.throttleFirst(500 , TimeUnit.MILLISECONDS)?.subscribe {
            animateCardOfSetting(settingLayout)
        }?.addTo(compositeDisposable)

        RxView.clicks(cardOfSetting)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {

            returnCardToOriginPositionOfSetting(650)

        }?.addTo(compositeDisposable)

        RxCompoundButton.checkedChanges(darkModeSwitch)?.skipInitialValue()?.throttleFirst(500 , TimeUnit.MILLISECONDS)?.subscribe {
            manageDarkMode(it)
        }?.addTo(compositeDisposable)

        RxCompoundButton.checkedChanges(localizationSwitch)?.throttleFirst(500 , TimeUnit.MILLISECONDS)?.subscribe {

        }?.addTo(compositeDisposable)

        RxView.clicks(goToFavouriteButton)?.throttleLatest(500, TimeUnit.MILLISECONDS)
            ?.subscribe {

                navigateToFavouriteScreen()
            }?.addTo(compositeDisposable)

        RxView.clicks(redmoreIcon)?.throttleLatest(500, TimeUnit.MILLISECONDS)
            ?.subscribe {
                navigateToMoreScreen(0)
            }?.addTo(compositeDisposable)

        RxView.clicks(greenmoreIcon)?.throttleLatest(500, TimeUnit.MILLISECONDS)
            ?.subscribe {
                navigateToMoreScreen(1)
            }?.addTo(compositeDisposable)

        RxView.clicks(ambermoreIcon)?.throttleLatest(500, TimeUnit.MILLISECONDS)
            ?.subscribe {
                navigateToMoreScreen(2)
            }?.addTo(compositeDisposable)

        RxView.clicks(graymoreIcon)?.throttleLatest(500, TimeUnit.MILLISECONDS)
            ?.subscribe {
                navigateToMoreScreen(3)
            }?.addTo(compositeDisposable)

        RxView.clicks(goToDetailOfMovieDetailButton)?.throttleLatest(500, TimeUnit.MILLISECONDS)
            ?.subscribe {
                returnCardToOriginPositionWithNavigationAction()

            }?.addTo(compositeDisposable)

        RxView.clicks(myProfile)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {
            animateCardOfProfile(myProfile)
        }?.addTo(compositeDisposable)

        RxView.clicks(meBody)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {

            returnCardToOriginPositionOfProfile(650)

        }?.addTo(compositeDisposable)

        RxView.clicks(cardOfPopular)?.throttleLatest(500, TimeUnit.MILLISECONDS)?.subscribe {

            returnCardToOriginPosition(650)

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

    private fun navigateToFavouriteScreen() {
        MovieCoordinator.navigateToFavouriteScreen(this@MainActivity, goToFavouriteButton)
    }

    private fun navigateToMoreScreen(position: Int? = 0) {
        MovieCoordinator.navigateToMoreScreen(this@MainActivity, position)
    }

    private fun navigateToDetailScreen(movieId: Int?) {
        MovieCoordinator.navigateToDetailScreen(this@MainActivity, movieId)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable?.dispose()
    }

    override fun onBackPressed() {

        if (meCard?.isGone == false) {
            returnCardToOriginPositionOfProfile(650)
        } else if (cardOfPopular?.isGone == false) {
            returnCardToOriginPosition(650)
        } else {
            this?.finish()
        }
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
            homeScreenRoot,
            getTransform(view, cardOfPopular, 650)
        )
        view?.isGone = true
        cardOfPopular?.isGone = false

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun animateCardOfProfile(view: View) {
        movieView = view
        android.transition.TransitionManager.beginDelayedTransition(
            homeScreenRoot,
            getTransform(view, meCard, 650)
        )
        view?.isGone = true
        meCard?.isGone = false

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun animateCardOfSetting(view: View) {
        movieView = view
        android.transition.TransitionManager.beginDelayedTransition(
            homeScreenRoot,
            getTransform(view, cardOfSetting, 650)
        )
        view?.isGone = true
        cardOfSetting?.isGone = false

    }

    private fun bindMovieDetailToPopularCard(movie: Result) {

        popularImagePoster?.loadImage( movie?.poster_path , movie?.title , popularImageLoading)
        popularCardName?.setText(movie?.title)
        popularCardDetail?.setText(movie?.overview)

    }

}
