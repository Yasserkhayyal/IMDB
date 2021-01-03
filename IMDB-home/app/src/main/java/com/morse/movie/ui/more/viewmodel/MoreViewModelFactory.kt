package com.morse.movie.ui.more.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoreViewModelFactory (private val moreAnnotateProcessor: MoreAnnotateProcessor) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoreViewModel( moreAnnotateProcessor ) as T
    }
}