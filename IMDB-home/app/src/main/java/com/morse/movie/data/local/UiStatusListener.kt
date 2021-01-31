package com.morse.movie.data.local

import kotlinx.coroutines.flow.Flow

interface UiStatusListener {

    public fun getCurrentLanguage () : Flow<String>

    suspend public fun changeCurrentLanguage (language: String)

    public fun getCurrentMode () : Flow<String>

    suspend public fun changeCurrentMode (mode: String)

}