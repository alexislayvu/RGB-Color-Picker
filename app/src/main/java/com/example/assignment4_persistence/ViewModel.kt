// Name: Alexis Vu
// CWID: 888697067
// Email: alexislayvu@csu.fullerton.edu

package com.example.assignment4_persistence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

const val INITIAL_RED_BAR_VALUE = 0
const val INITIAL_GREEN_BAR_VALUE = 0
const val INITIAL_BLUE_BAR_VALUE = 0

class ViewModel : ViewModel() {
    private var redProgress: Int = INITIAL_RED_BAR_VALUE
    private var greenProgress: Int = INITIAL_GREEN_BAR_VALUE
    private var blueProgress: Int = INITIAL_BLUE_BAR_VALUE

    private val prefs = PreferencesRepository.getRepository()

    // ---------- RED BAR ----------
    private fun saveRedProgress() {
        viewModelScope.launch {
            prefs.saveRedProgress(redProgress)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadRedProgress() {
        GlobalScope.launch {
            prefs.redProgress.collectLatest {
                redProgress = it
            }
        }
        sleep(1000)
    }

    fun getRedProgress(): Int {
        return this.redProgress
    }

    fun setRedBar(c: Int) {
        this.redProgress = c
        saveRedProgress()
    }


    // ---------- GREEN BAR ----------
    private fun saveGreenProgress() {
        viewModelScope.launch {
            prefs.saveGreenProgress(greenProgress)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadGreenProgress() {
        GlobalScope.launch {
            prefs.greenProgress.collectLatest {
                greenProgress = it
            }
        }
        sleep(1000)
    }

    fun getGreenProgress(): Int {
        return this.greenProgress
    }

    fun setGreenBar(c: Int) {
        this.greenProgress = c
        saveGreenProgress()
    }


    // ---------- BLUE BAR ----------
    private fun saveBlueProgress() {
        viewModelScope.launch {
            prefs.saveBlueProgress(blueProgress)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadBlueProgress() {
        GlobalScope.launch {
            prefs.blueProgress.collectLatest {
                blueProgress = it
            }
        }
        sleep(1000)
    }

    fun getBlueProgress(): Int {
        return this.blueProgress
    }

    fun setBlueBar(c: Int) {
        this.blueProgress = c
        saveBlueProgress()
    }
}