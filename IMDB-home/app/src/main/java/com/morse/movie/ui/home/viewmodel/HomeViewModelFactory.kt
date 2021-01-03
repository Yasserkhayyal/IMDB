package com.morse.movie.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory ( private val homeAnnotateProcessor: HomeAnnotateProcessor ) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(homeAnnotateProcessor) as T
    }

}