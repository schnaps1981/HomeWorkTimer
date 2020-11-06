package com.example.homeworktimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val notificationWorker = WorkManager.getInstance(application)
    private var counter = 0

    private val timer = MyTimer(500L) {
        counter++
        tvTicker.text = counter.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartTicker.setOnClickListener {
            timer.start()
        }

        btnStopTicker.setOnClickListener {
            timer.stop()
        }
    }

    private fun scheduleNotification() {
        val workerRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setInputData(createInputData())
            .build()

        notificationWorker.enqueue(workerRequest)
    }

    override fun onStop() {
        scheduleNotification()
        super.onStop()
    }

    private fun createInputData(): Data {
        val builder = Data.Builder()

        builder.putString(KEY_TIMER_VALUE, counter.toString())

        return builder.build()
    }

    companion object {
        const val KEY_TIMER_VALUE = "KEY_TIMER_VALUE"
    }
}