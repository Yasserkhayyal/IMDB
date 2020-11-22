package com.morse.movie.ui.home.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.morse.movie.R
import com.morse.movie.base.MviView
import com.morse.movie.ui.home.entities.HomeIntent
import com.morse.movie.ui.home.entities.HomeState
import com.morse.movie.ui.home.viewmodel.HomeViewModel
import com.morse.movie.utils.makeItOff
import com.morse.movie.utils.makeItOn
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        popularMoviesAdapter = MovieAdapter()
        topRatedMoviesAdapter = MovieAdapter()
        inComingMoviesAdapter = MovieAdapter()
        nowPlayingMoviesAdapter = MovieAdapter()
        bindViewModelWithView()
        configreAdapterToRecyclerview()
        popularMovieIntentSubject?.onNext(HomeIntent.LoadPopularMovies)
        topRatedMovieIntentSubject?.onNext(HomeIntent.LoadTopRatedMovies)
        inComingMovieIntentSubject?.onNext(HomeIntent.LoadIncomingMovies)
        nowPlayinggMovieIntentSubject?.onNext(HomeIntent.LoadNowPlayingMovies)
    }

    private fun configreAdapterToRecyclerview() {
        popularMoviesRecyclerView?.adapter = popularMoviesAdapter
        topRatedMoviesRecyclerView?.adapter = topRatedMoviesAdapter
        inComingMoviesRecyclerView?.adapter = inComingMoviesAdapter
        nowPlayingMoviesRecyclerView?.adapter = nowPlayingMoviesAdapter
    }

    override fun render(state: HomeState) {
        Log.i("toaster", Thread.currentThread().name)
        if (state?.isLoading == true) {
            if (state?.position == 0) {
                popularIndicatorView?.makeItOn()
            } else if (state?.position == 1) {
                topRatedIndicatorView?.makeItOn()
            } else if (state?.position == 2) {
                inComingIndicatorView?.makeItOn()
            } else if (state?.position == 3) {
                nowPlayingIndicatorView?.makeItOn()
            }
        } else if (state?.error != null) {
            if (state?.position == 0) {
                popularIndicatorView?.makeItOff()
            } else if (state?.position == 1) {
                topRatedIndicatorView?.makeItOff()
            } else if (state?.position == 2) {
                inComingIndicatorView?.makeItOff()
            } else if (state?.position == 3) {
                nowPlayingIndicatorView?.makeItOff()
            }
            Toast.makeText(this, " Error Because ${state?.error}", Toast.LENGTH_SHORT).show()
        } else if (state?.result != null) {
            popularIndicatorView?.makeItOff()
            if (state?.position == 0) {
                popularMoviesAdapter?.submitNewMovies(state?.result!!)
            } else if (state?.position == 1) {
                topRatedMoviesAdapter?.submitNewMovies(state?.result!!)
            } else if (state?.position == 2) {
                inComingMoviesAdapter?.submitNewMovies(state?.result!!)
            } else if (state?.position == 3) {
                nowPlayingMoviesAdapter?.submitNewMovies(state?.result!!)
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
            inComingMovieIntentSubject ,
            nowPlayinggMovieIntentSubject
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable?.dispose()
    }
}
