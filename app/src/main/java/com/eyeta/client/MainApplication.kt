
package com.eyeta.client

import androidx.multidex.MultiDexApplication
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Notification
import android.graphics.Color
import android.os.Build
import android.app.Activity
import com.eyeta.client.R

open class MainApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        System.setProperty("http.keepAliveDuration", (30 * 60 * 1000).toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun registerChannel() {
        val channel = NotificationChannel(
            PRIMARY_CHANNEL, getString(R.string.channel_default), NotificationManager.IMPORTANCE_LOW
        )
        channel.lightColor = Color.GREEN
        channel.lockscreenVisibility = Notification.VISIBILITY_SECRET
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
    }

    open fun handleRatingFlow(activity: Activity) {}

    companion object {
        const val PRIMARY_CHANNEL = "default"
    }

}
