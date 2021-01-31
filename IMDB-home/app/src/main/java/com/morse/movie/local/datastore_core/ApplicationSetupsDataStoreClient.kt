package com.morse.movie.local.datastore_core

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.createDataStore
import com.morse.movie.data.local.UiStatusListener
import com.morse.movie.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ApplicationSetupsDataStoreClient  (private val context: Context) : UiStatusListener{

    private var userDataStore = context?.createDataStore(userDataStoreNameKey)

    public override fun getCurrentLanguage(): Flow<String> {
        return userDataStore?.data?.catch { exception -> // 1
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) { // 2
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { it[LANGUAGE_KEY] ?: en }
    }

    suspend override public fun changeCurrentLanguage(language: String) {
        userDataStore.edit {
            it[LANGUAGE_KEY] = language
        }
    }

    public override fun getCurrentMode(): Flow<String> {
        return userDataStore?.data?.catch { exception -> // 1
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) { // 2
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { it[MODE_KEY] ?: light }
    }

    suspend override public fun changeCurrentMode(mode: String) {
        userDataStore.edit {
            it[MODE_KEY] = mode
        }
    }

}