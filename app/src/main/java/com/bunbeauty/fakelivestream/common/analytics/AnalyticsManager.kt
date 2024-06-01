package com.bunbeauty.fakelivestream.common.analytics

import android.util.Log
import com.bunbeauty.fakelivestream.common.util.toTimeString
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject
import javax.inject.Singleton

private const val STREAM_STARTED_EVENT = "stream_started"
private const val USERNAME_PARAM = "username"
private const val VIEWER_COUNT_PARAM = "viewer_count"

private const val VIEWER_COUNT_EVENT = "viewer_count_"

private const val STREAM_STOPPED_EVENT = "stream_stopped"
private const val STREAM_FINISHED_EVENT = "stream_finished"
private const val STREAM_DURATION_PARAM = "stream_duration"

private const val CAMERA_ERROR_EVENT = "camera_error"

private const val FEEDBACK_POSITIVE_EVENT = "feedback_positive"
private const val FEEDBACK_NEGATIVE_EVENT = "feedback_negative"

private const val SHARE_EVENT = "share"

private const val DONATE_EVENT = "donate"


private const val ANALYTICS_TAG = "analytics"

@Singleton
class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    private var startStreamTimeMillis: Long? = null

    fun trackStreamStart(username: String, viewerCount: Int) {
        startStreamTimeMillis = System.currentTimeMillis()

        firebaseAnalytics.logEvent(STREAM_STARTED_EVENT) {
            param(USERNAME_PARAM, username)
            param(VIEWER_COUNT_PARAM, viewerCount.toLong())
        }
        firebaseAnalytics.logEvent("$VIEWER_COUNT_EVENT$viewerCount") {}

        Log.d(ANALYTICS_TAG, "$STREAM_STARTED_EVENT: $username, ${viewerCount.toLong()}")
        Log.d(ANALYTICS_TAG, "$VIEWER_COUNT_EVENT$viewerCount")
    }

    fun trackStreamStop(durationInSeconds: Int) {
        val durationString = durationInSeconds.toTimeString()

        firebaseAnalytics.logEvent(STREAM_STOPPED_EVENT) {
            param(STREAM_DURATION_PARAM, durationString)
            param(FirebaseAnalytics.Param.VALUE, durationInSeconds.toLong())
        }

        Log.d(ANALYTICS_TAG, "$STREAM_STOPPED_EVENT: $durationString, ${durationInSeconds.toLong()}")
    }

    fun trackStreamFinish(durationInSeconds: Int) {
        val durationString = durationInSeconds.toTimeString()

        firebaseAnalytics.logEvent(STREAM_FINISHED_EVENT) {
            param(STREAM_DURATION_PARAM, durationString)
            param(FirebaseAnalytics.Param.VALUE, durationInSeconds.toLong())
        }

        Log.d(ANALYTICS_TAG, "$STREAM_FINISHED_EVENT: $durationString, ${durationInSeconds.toLong()}")
    }

    fun trackCameraError() {
        firebaseAnalytics.logEvent(CAMERA_ERROR_EVENT) {}
        Log.d(ANALYTICS_TAG, CAMERA_ERROR_EVENT)
    }

    fun trackFeedback(isPositive: Boolean) {
        val event = if (isPositive) {
            FEEDBACK_POSITIVE_EVENT
        } else {
            FEEDBACK_NEGATIVE_EVENT
        }

        firebaseAnalytics.logEvent(event) {}
        Log.d(ANALYTICS_TAG, event)
    }

    fun trackShare() {
        firebaseAnalytics.logEvent(SHARE_EVENT) {}
        Log.d(ANALYTICS_TAG, SHARE_EVENT)
    }

    fun trackDonate() {
        firebaseAnalytics.logEvent(DONATE_EVENT) {}
        Log.d(ANALYTICS_TAG, DONATE_EVENT)
    }

}