package com.morse.movie.app.root

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.flurry.android.FlurryAgent
import com.flurry.android.FlurryPerformance
import com.morse.movie.utils.FLURRY_API_KEY
import io.realm.Realm
import io.realm.RealmConfiguration

class MovieApplication : MultiDexApplication() {

    var realmInstance: Realm? = null
    var realmConfigration : RealmConfiguration? =null

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        realmConfigration = RealmConfiguration.Builder()
            .name("movieDB")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(realmConfigration)
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