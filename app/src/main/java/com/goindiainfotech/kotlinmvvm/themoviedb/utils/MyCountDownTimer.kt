package com.goindiainfotech.kotlinmvvm.themoviedb.utils

import android.os.CountDownTimer
import android.util.Log


class MyCountDownTimer(startTime: Long, interval: Long, private val func: () -> Unit) : CountDownTimer(startTime, interval) {
    override fun onFinish() = func()
    override fun onTick(timer: Long) { Log.d("TMDb","timer $timer")  }
}