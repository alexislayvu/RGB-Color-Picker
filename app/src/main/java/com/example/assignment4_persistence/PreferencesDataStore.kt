// Name: Alexis Vu
// CWID: 888697067
// Email: alexislayvu@csu.fullerton.edu

package com.example.assignment4_persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PreferencesRepository private constructor(private val dataStore: DataStore<Preferences>) {
    private val RED_KEY = intPreferencesKey("redProgress")
    private val GREEN_KEY = intPreferencesKey("greenProgress")
    private val BLUE_KEY = intPreferencesKey("blueProgress")

    // ---------- RED PREFERENCES ----------
    val redProgress: Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[RED_KEY] ?: INITIAL_RED_BAR_VALUE
    }.distinctUntilChanged()

    private suspend fun saveRedBarValue(key: Preferences.Key<Int>, value: Int) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun saveRedProgress(value: Int) {
        saveRedBarValue(RED_KEY, value)
    }


    // ---------- GREEN PREFERENCES ----------
    val greenProgress: Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[GREEN_KEY] ?: INITIAL_GREEN_BAR_VALUE
    }.distinctUntilChanged()

    private suspend fun saveGreenBarValue(key: Preferences.Key<Int>, value: Int) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun saveGreenProgress(value: Int) {
        saveGreenBarValue(GREEN_KEY, value)
    }


    // ---------- BLUE PREFERENCES ----------
    val blueProgress: Flow<Int> = this.dataStore.data.map { prefs ->
        prefs[BLUE_KEY] ?: INITIAL_BLUE_BAR_VALUE
    }.distinctUntilChanged()

    private suspend fun saveBlueBarValue(key: Preferences.Key<Int>, value: Int) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun saveBlueProgress(value: Int) {
        saveBlueBarValue(BLUE_KEY, value)
    }

    companion object {
        private const val PREFERENCES_DATA_FILE_NAME = "settings"
        private var INSTANCE: PreferencesRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile(PREFERENCES_DATA_FILE_NAME)
                }
                INSTANCE = PreferencesRepository(dataStore)
            }
        }

        fun getRepository(): PreferencesRepository {
            return INSTANCE
                ?: throw IllegalStateException("AppPreferencesRepository not initialized yet")
        }
    }
}