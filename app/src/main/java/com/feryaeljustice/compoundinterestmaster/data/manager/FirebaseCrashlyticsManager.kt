/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.data.manager

import android.util.Log
import com.feryaeljustice.compoundinterestmaster.domain.manager.CrashlyticsManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCrashlyticsManager @Inject constructor() : CrashlyticsManager {

    private val crashlytics: FirebaseCrashlytics by lazy {
        FirebaseCrashlytics.getInstance()
    }

    companion object {
        private const val TAG = "CrashlyticsManager"
    }

    override fun recordException(throwable: Throwable) {
        try {
            crashlytics.recordException(throwable)
            Log.d(TAG, "Excepcion registrada en Crashlytics: ${throwable.message}")
        } catch (e: Exception) {
            Log.w(TAG, "Error enviando excepcion a Crashlytics", e)
        }
    }

    override fun log(message: String) {
        try {
            crashlytics.log(message)
            Log.d(TAG, "Log registrado en Crashlytics: $message")
        } catch (e: Exception) {
            Log.w(TAG, "Error registrando log en Crashlytics", e)
        }
    }

    override fun setCustomKey(key: String, value: String) {
        try {
            crashlytics.setCustomKey(key, value)
        } catch (e: Exception) {
            Log.w(TAG, "Error configurando clave personalizada String en Crashlytics", e)
        }
    }

    override fun setCustomKey(key: String, value: Boolean) {
        try {
            crashlytics.setCustomKey(key, value)
        } catch (e: Exception) {
            Log.w(TAG, "Error configurando clave personalizada Boolean en Crashlytics", e)
        }
    }

    override fun setCustomKey(key: String, value: Double) {
        try {
            crashlytics.setCustomKey(key, value)
        } catch (e: Exception) {
            Log.w(TAG, "Error configurando clave personalizada Double en Crashlytics", e)
        }
    }

    override fun setCustomKey(key: String, value: Int) {
        try {
            crashlytics.setCustomKey(key, value)
        } catch (e: Exception) {
            Log.w(TAG, "Error configurando clave personalizada Int en Crashlytics", e)
        }
    }

    override fun setUserId(userId: String) {
        try {
            crashlytics.setUserId(userId)
        } catch (e: Exception) {
            Log.w(TAG, "Error configurando userId en Crashlytics", e)
        }
    }
}
