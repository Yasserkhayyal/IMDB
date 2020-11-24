package com.morse.movie.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.morse.movie.remote.entity.movieresponse.Result
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.wang.avi.AVLoadingIndicatorView
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.lang.Exception
import java.text.SimpleDateFormat

public fun String.bindFromDate(): String {
    if (this.length != 0) {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("MMM. dd, yyyy hh:mm a")
        val output: String = formatter.format(parser.parse(this))
        return output
    }
    return ""
}

public fun String.openWebsite ( context: Context ) {
    try {
        if(this?.length ==0){
         Toast.makeText(context , "There is`t an Valid Link to View , Sorry !" , Toast.LENGTH_SHORT).show()
        }
        else {
            var openWebsiteIntent = Intent()
            openWebsiteIntent?.action = Intent.ACTION_VIEW
            openWebsiteIntent?.data = Uri.parse(this)
            context?.startActivity(openWebsiteIntent)
        }
    }catch (e : Exception){
        Log.i("IMDB" , "Can`t Find Any App to Open Website")
    }
}

public fun DiscreteScrollView.addDiscreteViewConfig() {

    this.scrollToPosition(1)
    this?.setItemTransformer(
        ScaleTransformer.Builder()
            .setMaxScale(1.05f)
            .setMinScale(0.8f)
            .setPivotX(Pivot.X.CENTER) // CENTER is a default one
            .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
            .build()

    )
    this?.setSlideOnFling(true)
}

public fun String.bindFromDateForMovieReleqse(): String {
    if (this.length != 0) {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("MMM. dd, yyyy")
        val output: String = formatter.format(parser.parse(this))
        return output
    }
    return ""
}

public fun AVLoadingIndicatorView.makeItOn() {
    this?.visibility = View.VISIBLE
    this?.show()
}

public fun AVLoadingIndicatorView.makeItOff() {
    this?.visibility = View.INVISIBLE
    this?.hide()
}

public fun View.hideVisibilty() {
    this?.visibility = View.INVISIBLE
}

public fun View.showVisibilty() {
    this?.visibility = View.VISIBLE
}

