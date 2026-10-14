/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster

import android.app.Application
import com.feryaeljustice.compoundinterestmaster.domain.manager.CrashlyticsManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CompoundInterestApplication : Application() {

    @Inject
    lateinit var crashlyticsManager: CrashlyticsManager

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        crashlyticsManager.log("Application started")
        crashlyticsManager.setCustomKey("app_version_name", BuildConfig.VERSION_NAME)
        crashlyticsManager.setCustomKey("app_version_code", BuildConfig.VERSION_CODE)
    }
}