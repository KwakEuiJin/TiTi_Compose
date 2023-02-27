package com.timertiti.titi_compose

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    val isRunning = mutableStateOf(false)
    val time = mutableStateOf(180)

    fun startTimer() {
        isRunning.value = true
        CoroutineScope(Dispatchers.IO).launch {
            while (isRunning.value && time.value > 0) {
                delay(1000L)
                time.value -= 1
            }
        }
    }

    fun pauseTimer() {
        isRunning.value = false
    }

    fun resetTimer() {
        isRunning.value = false
        time.value = 180
    }
}
