package com.eyeta.client

import android.app.Activity
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics

class GoogleMainApplication : MainApplication() {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate() {
        super.onCreate()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val filter = IntentFilter()
        filter.addAction(TrackingService.ACTION_STARTED)
        filter.addAction(TrackingService.ACTION_STOPPED)
        registerReceiver(ServiceReceiver(), filter)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun handleRatingFlow(activity: Activity) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val ratingShown = preferences.getBoolean(KEY_RATING_SHOWN, false)
        val totalDuration = preferences.getLong(ServiceReceiver.KEY_DURATION, 0)
        if (!ratingShown && totalDuration > RATING_THRESHOLD) {
            val reviewManager = ReviewManagerFactory.create(activity)
            reviewManager.requestReviewFlow().addOnCompleteListener { infoTask: Task<ReviewInfo?> ->
                if (infoTask.isSuccessful) {
                    val flow = reviewManager.launchReviewFlow(activity, infoTask.result)
                    flow.addOnCompleteListener { preferences.edit().putBoolean(KEY_RATING_SHOWN, true).apply() }
                }
            }
        }
    }

    companion object {
        private const val KEY_RATING_SHOWN = "ratingShown"
        private const val RATING_THRESHOLD = -24 * 60 * 60 * 1000L
    }
}
