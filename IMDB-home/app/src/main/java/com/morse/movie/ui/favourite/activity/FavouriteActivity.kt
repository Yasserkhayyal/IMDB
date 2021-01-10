package com.morse.movie.ui.favourite.activity

import com.morse.movie.R
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.morse.movie.app.coordinator.MovieCoordinator
import com.morse.movie.base.MviView
import com.morse.movie.base.RecyclerViewShape
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.repository.DataRepositoryImpl
import com.morse.movie.domain.usecase.CheckIfMovieExistInDataBase
import com.morse.movie.domain.usecase.ClearAllMovies
import com.morse.movie.domain.usecase.LoadFavouriteMovies
import com.morse.movie.local.room_core.RoomClient
import com.morse.movie.local.room_core.RoomManager
import com.morse.movie.ui.favourite.entities.FavouriteIntent
import com.morse.movie.ui.favourite.entities.FavouriteStatus
import com.morse.movie.ui.favourite.viewmodel.FavouriteAnnotateProcessor
import com.morse.movie.ui.favourite.viewmodel.FavouriteViewModel
import com.morse.movie.ui.favourite.viewmodel.FavouriteViewModelFactory
import com.morse.movie.ui.home.activity.MovieAdapter
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_more_movies.*
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.util.concurrent.TimeUnit


class FavouriteActivity : AppCompatActivity(), MviView<FavouriteIntent, FavouriteStatus> {

    private val favouriteViewModel: FavouriteViewModel by lazy(LazyThreadSafetyMode.NONE) {

        val roomManager = RoomManager.invoke(this)
        val localSource = RoomClient(roomManager)
        //val localSource = RealmClient()
        //val dataManager = RetrofitMoreDataSourceManager()
        //val remoteSource = RetrofitClient(dataManager)
//        val dataManager = FuelMoreDataSourceManager()
//        val remoteSource = FuelClient(dataManager)
        val repository = DataRepositoryImpl(localeInterface = localSource)

        val checkIfExistInDatabase = CheckIfMovieExistInDataBase(repository)
        val loadAllMoviesInDatabase = LoadFavouriteMovies(repository)
        val removeAllMovieDetailResponseInDatabase = ClearAllMovies(repository)
        val favouriteAnnotateProcessor =
            FavouriteAnnotateProcessor(
                checkIfExistInDatabase,
                loadAllMoviesInDatabase,
                removeAllMovieDetailResponseInDatabase
            )

        val favouriteViewModelFactory = FavouriteViewModelFactory(favouriteAnnotateProcessor)
        ViewModelProviders.of(this, favouriteViewModelFactory).get(FavouriteViewModel::class.java)
    }

    private var checkMovieIfExistsIntentSubject =
        PublishSubject.create<FavouriteIntent.ChechIfMovieAlreadyExistIntent>()
    private var selectAllMoviesIntentSubject =
        PublishSubject.create<FavouriteIntent.LoadFavouriteMoviesListIntent>()
    private var removeAllMoviesIntentSubject =
        PublishSubject.create<FavouriteIntent.RemoveAllMoviesListIntent>()
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var popularMoviesAdapter: MovieAdapter
    var menuView : View ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setSupportActionBar(favouriteActivityActionBar)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        popularMoviesAdapter = MovieAdapter(RecyclerViewShape.VERTICAL, object : MovieListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onMovieClicks(movieCard: View, movieResult: Result, color: Int?) {
                MovieCoordinator.navigateToDetailScreen(this@FavouriteActivity, movieResult?.id)
            }
        })
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            homeNestedScrollView?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > oldScrollY) {
                    goToFavouriteButton?.shrink()
                } else {
                    goToFavouriteButton?.extend()
                }
            }
        }
        bindViewModelWithView()
        configreAdapterToRecyclerview()
        configureClicksOnButtons()
        requestOnAllMovies()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun configureClicksOnButtons() {
        favouriteActivityActionBar?.setNavigationOnClickListener {
            onBackPressed()
        }
        confirmRemoveDeletButton?.setOnClickListener {
            removeAllMoviesIntentSubject?.onNext(FavouriteIntent.RemoveAllMoviesListIntent)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(root, cardOfClearAllMovies, menuView!!, 650)
            }
        }
        cancelRemoveDeletButton?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(root, cardOfClearAllMovies, menuView!!, 650)
            }
        }
        cardOfClearAllMovies?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                returnCardToOriginPosition(root, cardOfClearAllMovies, menuView!!, 650)
            }
        }
    }

    private fun configreAdapterToRecyclerview() {
        favouriteMoviesRecyclerView?.adapter = popularMoviesAdapter
        favouriteMoviesRecyclerView?.layoutManager =
            AutoFitGridLayoutManager(this, 400)
    }

    override fun onBackPressed() {
        if (cardOfClearAllMovies?.isGone == false) {
            returnCardToOriginPosition(
                root,
                cardOfClearAllMovies,
                menuView!!,
                650
            )
        } else {
            this?.finish()
        }
    }

    override fun render(state: FavouriteStatus) {

        if (state?.isSelectLoading == true) {
            favouriteMoviesLoading?.makeItOn()
            favouriteMoviesErrorContainer?.hideVisibilty()
            favouriteMoviesRecyclerView?.hideVisibilty()
        } else if (state?.deleteAllMoviesError != null || state?.selectAllMoviesError != null) {
            favouriteMoviesLoading?.makeItOff()
            favouriteMoviesErrorContainer?.hideVisibilty()
            favouriteMoviesRecyclerView?.hideVisibilty()

        } else if (state?.selectedData != null) {
            favouriteMoviesLoading?.makeItOff()
            if (state?.selectedData?.size == 0) {
                favouriteMoviesErrorContainer?.showVisibilty()
                favouriteMoviesRecyclerView?.hideVisibilty()
            } else {
                var resultMovieData = changeResultToMovieResponse(state?.selectedData!!)
                favouriteMoviesErrorContainer?.hideVisibilty()
                favouriteMoviesRecyclerView?.showVisibilty()
                popularMoviesAdapter?.submitNewMovies(resultMovieData)
            }

        } else if (state?.isDeletedFinished != null) {
            favouriteMoviesLoading?.makeItOff()
            favouriteMoviesErrorContainer?.showVisibilty()
            favouriteMoviesRecyclerView?.hideVisibilty()
            popularMoviesAdapter?.clearAllMovies()


        }
    }

    private fun changeResultToMovieResponse(listOfMovies: ArrayList<MovieDetailResponse>): ArrayList<Result> {
        return listOfMovies?.map {
            Result(
                adult = it?.adult,
                backdrop_path = it?.backdrop_path,
                id = it?.id,
                original_language = it?.original_language,
                original_title = it?.original_title,
                overview = it?.overview,
                popularity = it?.popularity,
                poster_path = it?.poster_path,
                release_date = it?.release_date,
                title = it?.title,
                video = it?.video,
                vote_average = it?.vote_average,
                vote_count = it?.vote_count
            )
        } as ArrayList<Result>
    }

    private fun bindViewModelWithView() {
        compositeDisposable?.add(favouriteViewModel?.getStatus()?.subscribe(::render))
        favouriteViewModel?.processIntents(collectOurIntents())
    }

    private fun requestOnAllMovies() {
        if (popularMoviesAdapter?.itemCount == 0) {
            selectAllMoviesIntentSubject?.onNext(FavouriteIntent.LoadFavouriteMoviesListIntent)
        }
    }

    override fun collectOurIntents(): Observable<FavouriteIntent> {
        return Observable.merge(
            checkMovieIfExistsIntentSubject,
            selectAllMoviesIntentSubject,
            removeAllMoviesIntentSubject
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        val spinnerItem = menu!!.findItem(R.id.clearFav)
        menuView = spinnerItem.getActionView() as AppCompatImageButton
    //    menuView?.setBackgroundColor( resources?.getColor(android.R.color.transparent)!! )
        menuView?.setBackgroundResource(R.drawable.ic_baseline_clear_all_24)
        menuView?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (cardOfPopularDetail?.visibility == View.VISIBLE) {
                    returnCardToOriginPosition(
                        root,
                        cardOfClearAllMovies,
                        menuView!!,
                        200
                    )
                    Observable.intervalRange(0, 100, 250, 0, TimeUnit.MILLISECONDS)
                        ?.subscribeOn(AndroidSchedulers.mainThread())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe {
                            animateCard(root, cardOfClearAllMovies, menuView!!)
                        }?.addTo(compositeDisposable)
                } else {
                    animateCard(root, cardOfClearAllMovies, menuView!!)
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return return when (item.itemId) {
            R.id.clearFav -> {
                menuView = item?.actionView
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (cardOfPopularDetail?.visibility == View.VISIBLE) {
                        returnCardToOriginPosition(
                            root,
                            cardOfClearAllMovies,
                            menuView!!,
                            200
                        )
                        Observable.intervalRange(0, 100, 250, 0, TimeUnit.MILLISECONDS)
                            ?.subscribeOn(AndroidSchedulers.mainThread())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe {
                                animateCard(root, cardOfClearAllMovies, menuView!!)
                            }?.addTo(compositeDisposable)
                    } else {
                        animateCard(root, cardOfClearAllMovies, menuView!!)
                    }
                }


                true
            }
            else -> false
        }
    }

    override fun onStop() {
        super.onStop()
        if (cardOfClearAllMovies?.isGone == false) {
            returnCardToOriginPosition(
                root,
                cardOfClearAllMovies,
                menuView!!,
                650
            )
        }
        compositeDisposable?.dispose()
    }

    //---------------------------------------------------------- Animate Clear All Movies Animation ------------------------------
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun returnCardToOriginPositionWithNavigationAction(
        layoutRoot: ViewGroup,
        card: View,
        adapterView: View
    ) {
        android.transition.TransitionManager.beginDelayedTransition(
            layoutRoot,
            getTransform(card, adapterView, 650)
        )
        card?.isGone = true
        adapterView?.isGone = false
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