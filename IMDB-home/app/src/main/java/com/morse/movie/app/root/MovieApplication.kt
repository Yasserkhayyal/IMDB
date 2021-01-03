package com.morse.movie.app.root

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.flurry.android.FlurryAgent
import com.flurry.android.FlurryPerformance
import com.morse.movie.utils.FLURRY_API_KEY

class MovieApplication : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        setupFlurry()
    }

    private fun setupFlurry (){
        FlurryAgent.Builder()
            .withLogEnabled(true)
            .withCaptureUncaughtExceptions(true)
            .withIncludeBackgroundSessionsInMetrics(true)
            .withLogLevel(Log.ERROR)
            .withPerformanceMetrics(FlurryPerformance.ALL)
            .build(this, FLURRY_API_KEY)
    }

}