package com.example.foodapp.crash

import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashLogger(
    private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
) {
    fun logNonFatalError(place: String, error: Throwable) {
        crashlytics.setCustomKey("place", place)
        crashlytics.recordException(error)
    }

    fun logMessage(message: String) {
        crashlytics.log(message)
    }
}
