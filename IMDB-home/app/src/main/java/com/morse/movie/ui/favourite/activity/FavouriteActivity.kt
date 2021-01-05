package com.morse.movie.ui.favourite.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.morse.movie.R
import com.morse.movie.app.coordinator.MovieCoordinator
import com.morse.movie.base.MviView
import com.morse.movie.base.RecyclerViewShape
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.data.entity.movieresponse.Result
import com.morse.movie.data.repository.DataRepositoryImpl
import com.morse.movie.domain.usecase.CheckIfMovieExistInDataBase
import com.morse.movie.domain.usecase.LoadFavouriteMovies
import com.morse.movie.local.realm_core.RealmClient
import com.morse.movie.local.room_core.RoomClient
import com.morse.movie.local.room_core.RoomManager
import com.morse.movie.remote.fuel_core.core.FuelClient
import com.morse.movie.remote.retrofit_core.datasource.manager.FuelMoreDataSourceManager
import com.morse.movie.ui.favourite.entities.FavouriteIntent
import com.morse.movie.ui.favourite.entities.FavouriteStatus
import com.morse.movie.ui.favourite.viewmodel.FavouriteAnnotateProcessor
import com.morse.movie.ui.favourite.viewmodel.FavouriteViewModel
import com.morse.movie.ui.favourite.viewmodel.FavouriteViewModelFactory
import com.morse.movie.ui.home.activity.MovieAdapter
import com.morse.movie.ui.home.activity.MovieListener
import com.morse.movie.utils.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_more_movies.*

class FavouriteActivity : AppCompatActivity(), MviView<FavouriteIntent, FavouriteStatus> {

    private val favouriteViewModel: FavouriteViewModel by lazy(LazyThreadSafetyMode.NONE) {

        //val roomManager = RoomManager.invoke(this)
        //val localSource = RoomClient(roomManager)
        val localSource = RealmClient()
        //val dataManager = RetrofitMoreDataSourceManager()
        //val remoteSource = RetrofitClient(dataManager)
        val dataManager = FuelMoreDataSourceManager()
        val remoteSource = FuelClient(dataManager)
        val repository = DataRepositoryImpl(remoteSource, localSource)

        val checkIfExistInDatabase = CheckIfMovieExistInDataBase(repository)
        val loadAllMoviesInDatabase = LoadFavouriteMovies(repository)

        val favouriteAnnotateProcessor =
            FavouriteAnnotateProcessor(checkIfExistInDatabase, loadAllMoviesInDatabase)

        val favouriteViewModelFactory = FavouriteViewModelFactory(favouriteAnnotateProcessor)
        ViewModelProviders.of(this, favouriteViewModelFactory).get(FavouriteViewModel::class.java)
    }

    private var checkMovieIfExistsIntentSubject =
        PublishSubject.create<FavouriteIntent.ChechIfMovieAlreadyExistIntent>()
    private var selectAllMoviesIntentSubject =
        PublishSubject.create<FavouriteIntent.LoadFavouriteMoviesListIntent>()
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var popularMoviesAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
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
    }

    private fun configreAdapterToRecyclerview() {
        favouriteMoviesRecyclerView?.adapter = popularMoviesAdapter
        favouriteMoviesRecyclerView?.layoutManager =
            AutoFitGridLayoutManager(this, 400)
    }

    override fun onBackPressed() {
        this?.finish()
    }

    override fun render(state: FavouriteStatus) {

        if (state?.isSelectLoading == true) {
            favouriteMoviesLoading?.makeItOn()
            favouriteMoviesErrorContainer?.hideVisibilty()
            favouriteMoviesRecyclerView?.hideVisibilty()
        } else if (state?.error != null) {
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
            selectAllMoviesIntentSubject
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return return when (item.itemId) {
            R.id.clearFav -> {
                Toast.makeText(this, "Clear all Favourites", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable?.dispose()
    }
}