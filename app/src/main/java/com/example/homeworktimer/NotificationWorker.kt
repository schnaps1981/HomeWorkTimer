package com.example.homeworktimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.homeworktimer.MainActivity.Companion.KEY_TIMER_VALUE
import kotlin.random.Random


class NotificationWorker(
    private val context: Context,
    private val params: WorkerParameters
) : Worker(context, params) {
    private val channelId = "cft.homework"

    override fun doWork(): Result {
        params.inputData.getString(KEY_TIMER_VALUE)?.let {
            startNotification(it, "Значение таймера")
        }

        return Result.success()
    }

    private fun startNotification(text: String, title: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Таймер",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val intent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val notification = NotificationCompat
            .Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(intent)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        notificationManager.notify(Random.nextInt(10000), notification)
    }
}