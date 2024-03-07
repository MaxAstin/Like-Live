package com.bunbeauty.fakelivestream

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FakeLiveStreamApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Firebase.analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}