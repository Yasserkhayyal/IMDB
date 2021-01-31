package com.morse.movie.app.coordinator

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialElevationScale
import com.morse.movie.R
import com.morse.movie.data.entity.moviedetailresponse.MovieDetailResponse
import com.morse.movie.ui.detail.activity.MovieDetailActivity
import com.morse.movie.ui.favourite.activity.FavouriteActivity
import com.morse.movie.ui.more.activity.MoreMoviesActivity
import com.morse.movie.utils.MOVIE_DETAIL_ID_kEY
import com.morse.movie.utils.MOVIE_ID_kEY
import com.morse.movie.utils.MOVIE_TYPE

object MovieCoordinator {

    public fun navigateToDetailScreen(currentActivity: Activity, movieId: Int?) {
        var detailIntent = Intent(currentActivity, MovieDetailActivity::class.java)
        detailIntent?.putExtra(MOVIE_ID_kEY, movieId)
        currentActivity?.startActivity(detailIntent)
    }

    public fun navigateToFavouriteScreen(currentActivity: Activity, goToFavouriteButton: View) {
        var favIntent = Intent(currentActivity, FavouriteActivity::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            var options = ActivityOptions.makeSceneTransitionAnimation(
                currentActivity,
                goToFavouriteButton,
                "favShared"
            )
            currentActivity?.startActivity(favIntent, options.toBundle());
        } else {
            currentActivity?.startActivity(favIntent)
        }
        currentActivity?.window.sharedElementExitTransition = MaterialElevationScale(false).apply {
            duration = 650L
        }
        currentActivity?.window?.sharedElementReenterTransition =
            MaterialElevationScale(true).apply {
                duration = 650L
            }
    }

    public fun navigateToMoreScreen(currentActivity: Activity, position: Int? = 0) {
        var moreIntent = Intent(currentActivity, MoreMoviesActivity::class.java)
        moreIntent?.putExtra(MOVIE_TYPE, position)
        currentActivity?.startActivity(moreIntent)

    }

    public fun openDeleteSnackbar(parentView: View, movieName: String?) {
        var favSnackbar = Snackbar.make(
            parentView,
            "${movieName} has been deleted successfully",
            Snackbar.LENGTH_SHORT
        )
        var view = favSnackbar.getView()
        var params = view.getLayoutParams() as (CoordinatorLayout.LayoutParams)
        params.gravity = Gravity.BOTTOM
        view.setLayoutParams(params)
        favSnackbar?.setGestureInsetBottomIgnored(true)
        favSnackbar?.animationMode = Snackbar.ANIMATION_MODE_FADE
        favSnackbar?.setTextColor(parentView?.resources?.getColor(android.R.color.white)!!)
        favSnackbar?.setBackgroundTint(parentView?.resources?.getColor(android.R.color.holo_red_dark)!!)
        favSnackbar.show()
    }

    public fun openAddedSnackbar(parentView: View, movieName: String?) {
        var favSnackbar = Snackbar.make(
            parentView,
            "${movieName} has been added successfully",
            Snackbar.LENGTH_SHORT
        )
        var view = favSnackbar.getView()
        var params = view.getLayoutParams() as (CoordinatorLayout.LayoutParams)
        params.gravity = Gravity.BOTTOM
        view.setLayoutParams(params)
        favSnackbar?.setGestureInsetBottomIgnored(true)
        favSnackbar?.animationMode = Snackbar.ANIMATION_MODE_FADE
        favSnackbar?.setTextColor(parentView?.resources?.getColor(android.R.color.white)!!)
        favSnackbar?.setBackgroundTint(parentView?.resources?.getColor(R.color.green)!!)
        favSnackbar.show()


    }

    public fun openFlashbarSnackbar(
        activity: Activity,
        movieName: String,
        movieMessage: String,
        @DrawableRes backgroundResources: Int,
        @DrawableRes iconResources: Int
    ) {
        var ourFlashbar = Flashbar.Builder(activity)
            .gravity(Flashbar.Gravity.TOP)

            .title(movieName)
            .message(movieMessage)
            .duration(Flashbar.DURATION_LONG)
            .vibrateOn(Flashbar.Vibration.SHOW)
            .enableSwipeToDismiss()
            .backgroundDrawable(backgroundResources)
            .enterAnimation(
                FlashAnim.with(activity)
                    .animateBar()
                    .duration(750)
                    .alpha()
                    .overshoot()
            )
            .exitAnimation(
                FlashAnim.with(activity)
                    .animateBar()
                    .duration(400)
                    .accelerateDecelerate()
            )
            .showIcon()
            .icon(iconResources)
            .iconAnimation(FlashAnim.with(activity)
                .animateIcon()
                .pulse()
                .alpha()
                .duration(750)
                .accelerate())
            .build()
        ourFlashbar?.show()
    }

}