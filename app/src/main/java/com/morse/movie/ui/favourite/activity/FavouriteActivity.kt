package com.morse.movie.ui.favourite.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.morse.movie.R
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.activity_more_movies.*

class FavouriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        favouriteActivityActionBar?.setNavigationOnClickListener {
            this.finish()
        }
    }
}