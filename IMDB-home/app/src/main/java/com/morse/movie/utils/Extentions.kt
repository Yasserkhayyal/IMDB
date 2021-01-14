package com.morse.movie.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import com.morse.movie.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.transition.Slide
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.wang.avi.AVLoadingIndicatorView
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.person_review_layout.*
import java.lang.Exception
import java.text.SimpleDateFormat

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public fun Activity.returnCardToOriginPositionWithNavigationAction(layoutRoot : ViewGroup, card : View, adapterView : View , navigationFunction : (movieId : Int)-> Unit  , currentMovieId : Int) {
    android.transition.TransitionManager.beginDelayedTransition(
        layoutRoot,
        getTransform(card, adapterView, 650)
    )
    card?.isGone = true
    adapterView?.isGone = false
    navigationFunction(currentMovieId)
}

// return a transformation already < DONE >
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public fun Activity.getTransform(
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
public fun Activity.animateCard(layoutRoot : ViewGroup, card : View, adapterView : View) {
    //movieView = adapterView
    android.transition.TransitionManager.beginDelayedTransition(
        layoutRoot,
        getTransform(adapterView, card, 650)
    )
    adapterView?.isGone = true
    card?.isGone = false

}

// close card and show item of recyclerview
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public fun Activity.returnCardToOriginPosition(layoutRoot : ViewGroup, card : View, adapterView : View, duration: Long) {
    android.transition.TransitionManager.beginDelayedTransition(
        layoutRoot,
        getTransform(card, adapterView, duration)
    )
    card?.isGone = true
    adapterView?.isGone = false
}

public fun String.bindFromDate(): String {
    if (this.length != 0) {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("MMM. dd, yyyy hh:mm a")
        val output: String = formatter.format(parser.parse(this))
        return output
    }
    return ""
}

public fun String.copyText(context: Context ) {

    val clipboard: ClipboardManager? =
        context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", this)
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(context, "${ this } has been Copied to the clipboard ", Toast.LENGTH_SHORT).show()
}

public fun String.openYoutubeVideo ( context: Context) {
    try {
        if(this?.length ==0){
            Toast.makeText(context , "There is`t an Valid Link to View , Sorry !" , Toast.LENGTH_SHORT).show()
        }
        else {
            var openWebsiteIntent = Intent()
            openWebsiteIntent?.action = Intent.ACTION_VIEW
            openWebsiteIntent?.data = Uri.parse(youtubeVideos + this)
            context?.startActivity(openWebsiteIntent)
        }
    }catch (e : Exception){
        Toast.makeText(context , "Can`t Find Any App to Open Website" , Toast.LENGTH_SHORT).show()
        Log.i("IMDB" , "Can`t Find Any App to Open Website")
    }
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
        Toast.makeText(context , "Can`t Find Any App to Open Website" , Toast.LENGTH_SHORT).show()
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

public fun AppCompatActivity.applyMaterialTransform(transitionName: String) {
    window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    ViewCompat.setTransitionName(findViewById(android.R.id.content), transitionName)

    // set up shared element transition
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = getContentTransform(this)
        window.sharedElementReturnTransition = Slide().apply {
            duration = 1000L
            addTarget(R.id.goToFavouriteButton)
        }
    }
}

internal fun getContentTransform(activity: Activity): MaterialContainerTransform {
    return MaterialContainerTransform().apply {
       // this?.startView = activity?.goToFavouriteButton
        this?.endView = activity?.root
        //this.addTarget(R.id.root)
        this.setDuration(1000L)
        this.pathMotion = MaterialArcMotion()
        this?.scrimColor = Color.TRANSPARENT
        this.isElevationShadowEnabled = true
        this.setAllContainerColors(Color.TRANSPARENT)
    }
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
    this?.visibility = View.GONE
}

public fun View.showVisibilty() {
    this?.visibility = View.VISIBLE
}
//Unrecognized type of request: Request{ https://avatars.dicebear.com/4.5/api/bottts/TheReckoning.svg?r=50&colors[]=red&sidesChance=32 RoundedTransformation(radius=0, margin=0, diameter=0, cornerType=ALL)}
public fun ImageView.loadImageAsBackground (url : String ? ="", text : String? ="" , avlLoadingView : AVLoadingIndicatorView){
    if(url == null){
        val newUrl = emptyPlaceHolderHeader + text + emptyPlaceholderBackgroundBody
        Picasso.get()?.load(newUrl)?.transform(
            RoundedCornersTransformation(0, 0)
        )
            ?.into(this, object : Callback {
            override fun onSuccess() {
                avlLoadingView?.makeItOff()
            }

            override fun onError(e: Exception?) {
                avlLoadingView?.makeItOff()
            }

        })
    }
    else{
        Picasso.get()?.load(imageApiBackground + url)?.transform(
            RoundedCornersTransformation(0, 0)
        )?.into(this, object : Callback {
            override fun onSuccess() {
                avlLoadingView?.makeItOff()
            }

            override fun onError(e: Exception?) {
                avlLoadingView?.makeItOff()
            }

        })
    }
}

public fun ImageView.loadImage (url : String ? ="", text : String? ="" , avlLoadingView : AVLoadingIndicatorView){
    if(url == null){
        val newUrl = emptyPlaceHolderHeader + text + emptyPlaceholderBody
        Picasso.get()?.load(newUrl)?.transform(
            RoundedCornersTransformation(20, 20)
        ) ?.into(this, object : Callback {
            override fun onSuccess() {
                avlLoadingView?.makeItOff()
            }

            override fun onError(e: Exception?) {
                avlLoadingView?.makeItOff()
            }

        })
    }
    else{
        Picasso.get()?.load(imageApiBackground + url)?.transform(
            RoundedCornersTransformation(20, 20)
        )?.into(this, object : Callback {
            override fun onSuccess() {
                avlLoadingView?.makeItOff()
            }

            override fun onError(e: Exception?) {
                avlLoadingView?.makeItOff()
            }

        })
    }
}

public fun ImageView.loadUserImage (url : String ? ="", text : String? ="" , avlLoadingView : AVLoadingIndicatorView){
    if(url == null || url?.isEmpty() ==true){
        val newUrl = emptyPlaceHolderHeader + text + emptyPlaceholderBody
        Picasso.get()?.load(newUrl)?.transform(
            RoundedCornersTransformation(20 , 10)
        )?.into(this, object : Callback {
            override fun onSuccess() {
                avlLoadingView?.makeItOff()
            }

            override fun onError(e: Exception?) {
                avlLoadingView?.makeItOff()
            }

        })
    }
    else if (url?.contains("https") == true){
        var urlRes = url?.removeRange(0,1)
        Picasso.get()?.load( urlRes )?.transform(
            RoundedCornersTransformation(20 , 10)
        )?.into(this, object : Callback {
            override fun onSuccess() {
                avlLoadingView?.makeItOff()
            }

            override fun onError(e: Exception?) {
                avlLoadingView?.makeItOff()
            }

        })
    }
    else{
        Picasso.get()?.load(  imageApiPoster + url )?.transform(
            RoundedCornersTransformation(20 , 10)
        )?.into(this, object : Callback {
            override fun onSuccess() {
                avlLoadingView?.makeItOff()
            }

            override fun onError(e: Exception?) {
                avlLoadingView?.makeItOff()
            }

        })
    }
}

