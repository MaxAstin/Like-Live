package com.bunbeauty.fakelivestream.common.analytics

import com.bunbeauty.fakelivestream.common.util.toTimeString
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject
import javax.inject.Singleton

private const val STREAM_STARTED = "stream_started"
private const val USERNAME_PARAM = "username"
private const val VIEWER_COUNT_PARAM = "viewer_count"

private const val STREAM_STOPPED = "stream_stopped"
private const val STREAM_FINISHED = "stream_finished"
private const val STREAM_DURATION_PARAM = "stream_duration"

@Singleton
class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    private var startStreamTimeMillis: Long? = null

    fun trackStreamStart(username: String, viewerCount: Int) {
        startStreamTimeMillis = System.currentTimeMillis()

        firebaseAnalytics.logEvent(STREAM_STARTED) {
            param(USERNAME_PARAM, username)
            param(VIEWER_COUNT_PARAM, viewerCount.toLong())
        }
    }

    fun trackStreamResumption() {
        startStreamTimeMillis = System.currentTimeMillis()
    }

    fun trackStreamStop() {
        val durationInSeconds = getStreamDurationInSeconds() ?: return
        startStreamTimeMillis = null

        firebaseAnalytics.logEvent(STREAM_STOPPED) {
            param(STREAM_DURATION_PARAM, durationInSeconds.toTimeString())
            param(FirebaseAnalytics.Param.VALUE, durationInSeconds)
        }
    }

    fun trackStreamFinish() {
        val durationInSeconds = getStreamDurationInSeconds() ?: return
        startStreamTimeMillis = null

        firebaseAnalytics.logEvent(STREAM_FINISHED) {
            param(STREAM_DURATION_PARAM, durationInSeconds.toTimeString())
            param(FirebaseAnalytics.Param.VALUE, durationInSeconds)
        }
    }

    private fun getStreamDurationInSeconds(): Long? {
        val start = startStreamTimeMillis ?: return null
        val finish = System.currentTimeMillis()

        return (finish - start) / 1000
    }

}