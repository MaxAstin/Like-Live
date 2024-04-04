package com.bunbeauty.fakelivestream.common.analytics

import com.bunbeauty.fakelivestream.common.util.toTimeString
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject
import javax.inject.Singleton

private const val STREAM_STARTED_EVENT = "stream_started"
private const val USERNAME_PARAM = "username"
private const val VIEWER_COUNT_PARAM = "viewer_count"

private const val STREAM_STOPPED_EVENT = "stream_stopped"
private const val STREAM_FINISHED_EVENT = "stream_finished"
private const val STREAM_DURATION_PARAM = "stream_duration"

private const val FEEDBACK_POSITIVE_EVENT = "feedback_positive"
private const val FEEDBACK_NEGATIVE_EVENT = "feedback_negative"

private const val SHARE_EVENT = "share"

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
    }

    fun trackStreamStop(durationInSeconds: Int) {
        firebaseAnalytics.logEvent(STREAM_STOPPED_EVENT) {
            param(STREAM_DURATION_PARAM, durationInSeconds.toTimeString())
            param(FirebaseAnalytics.Param.VALUE, durationInSeconds.toLong())
        }
    }

    fun trackStreamFinish(durationInSeconds: Int) {
        firebaseAnalytics.logEvent(STREAM_FINISHED_EVENT) {
            param(STREAM_DURATION_PARAM, durationInSeconds.toTimeString())
            param(FirebaseAnalytics.Param.VALUE, durationInSeconds.toLong())
        }
    }

    fun trackFeedback(isPositive: Boolean) {
        if (isPositive) {
            firebaseAnalytics.logEvent(FEEDBACK_POSITIVE_EVENT) {}
        } else {
            firebaseAnalytics.logEvent(FEEDBACK_NEGATIVE_EVENT) {}
        }
    }

    fun trackShare() {
        firebaseAnalytics.logEvent(SHARE_EVENT) {}
    }

}