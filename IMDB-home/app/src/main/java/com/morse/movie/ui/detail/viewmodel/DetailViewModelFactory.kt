package com.morse.movie.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailViewModelFactory (private val dedetailAnnotateProcessor: DetailAnnotateProcessor) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel (dedetailAnnotateProcessor) as T
    }
}