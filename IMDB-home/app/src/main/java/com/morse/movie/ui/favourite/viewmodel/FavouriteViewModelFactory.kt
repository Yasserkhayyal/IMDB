package com.morse.movie.ui.favourite.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavouriteViewModelFactory (private val favouriteAnnotateProcessor: FavouriteAnnotateProcessor) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouriteViewModel (favouriteAnnotateProcessor) as T
    }
}