package com.example.homeworktimer

import android.os.Handler
import android.os.Looper
import kotlin.concurrent.thread

class MyTimer(
    private val interval: Long = 1000L,
    private val isStarted: Boolean = false,
    private val runnable: () -> Unit
) {
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var isPaused = true

    private val thread = thread(start = isStarted, isDaemon = false, name = "MyThread") {
        while (true) {
            if (isPaused) continue
            handler.post {
                runnable.invoke()
            }
            Thread.sleep(interval)
        }
    }

    init {
        isPaused = !isStarted
    }

    fun start() {
        if (!thread.isAlive) {
            thread.start()
        }

        if (isPaused) {
            isPaused = false
        }
    }

    fun stop() {
        isPaused = true
    }

}