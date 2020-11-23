package com.morse.movie.utils

import android.view.View
import com.wang.avi.AVLoadingIndicatorView
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
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