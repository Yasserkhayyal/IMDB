package com.morse.movie.utils

import android.view.View
import com.wang.avi.AVLoadingIndicatorView
import java.text.SimpleDateFormat

public fun String.bindFromDate () : String{
    if (this.length != 0) {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("MMM. dd, yyyy hh:mm a")
        val output: String = formatter.format(parser.parse(this))
        return output
    }
    return ""
}

public fun String.bindFromDateForMovieReleqse () : String{
    if (this.length != 0) {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("MMM. dd, yyyy")
        val output: String = formatter.format(parser.parse(this))
        return output
    }
    return ""
}

public fun AVLoadingIndicatorView.makeItOn () {
        this?.show()
}

public fun AVLoadingIndicatorView.makeItOff() {
        this?.hide()
}

public fun View.manageVisibilty (){
    if ( this?.visibility == View.VISIBLE ){
        this?.visibility = View.INVISIBLE
    }
    else {
        this?.visibility = View.VISIBLE
    }
}