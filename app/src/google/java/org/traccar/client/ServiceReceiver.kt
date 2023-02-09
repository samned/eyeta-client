
package com.eyeta.client

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager

class ServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (TrackingService.ACTION_STARTED == intent.action) {
            startTime = System.currentTimeMillis()
        } else if (startTime > 0) {
            updateTime(context, System.currentTimeMillis() - startTime)
            startTime = 0
        }
    }

    private fun updateTime(context: Context, duration: Long) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val totalDuration = preferences.getLong(KEY_DURATION, 0)
        preferences.edit().putLong(KEY_DURATION, totalDuration + duration).apply()
    }

    companion object {
        const val KEY_DURATION = "serviceTime"
        private var startTime: Long = 0
    }
}
