/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.data.manager

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.feryaeljustice.compoundinterestmaster.domain.manager.AnalyticsManager
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AnalyticsManager {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    companion object {
        private const val TAG = "AnalyticsManager"
        private const val EVENT_CALCULATION_PERFORMED = "calculation_performed"
        private const val PARAM_CALCULATION_TYPE = "calculation_type"
        private const val PARAM_INITIAL_CAPITAL = "initial_capital"
        private const val PARAM_ANNUAL_RATE = "annual_rate"
        private const val PARAM_YEARS = "years"
        private const val PARAM_CURRENCY = "currency"
    }

    override fun logEvent(name: String, params: Bundle?) {
        try {
            firebaseAnalytics.logEvent(name, params)
            Log.d(TAG, "Evento registrado: $name")
        } catch (e: Exception) {
            Log.w(TAG, "Error registrando evento $name en Firebase Analytics", e)
        }
    }

    override fun logCalculationPerformed(
        calculationType: String,
        initialCapital: Double,
        annualRate: Double,
        years: Int,
        currencyCode: String
    ) {
        val bundle = Bundle().apply {
            putString(PARAM_CALCULATION_TYPE, calculationType)
            putDouble(PARAM_INITIAL_CAPITAL, initialCapital)
            putDouble(PARAM_ANNUAL_RATE, annualRate)
            putInt(PARAM_YEARS, years)
            putString(PARAM_CURRENCY, currencyCode)
        }
        logEvent(EVENT_CALCULATION_PERFORMED, bundle)
    }

    override fun logCalculationTypeSelected(calculationType: String) {
        val bundle = Bundle().apply {
            putString(PARAM_CALCULATION_TYPE, calculationType)
        }
        logEvent("calculation_type_selected", bundle)
    }

    override fun logCurrencySelected(currencyCode: String) {
        val bundle = Bundle().apply {
            putString(PARAM_CURRENCY, currencyCode)
        }
        logEvent("currency_selected", bundle)
    }

    override fun logAdImpression(adUnitId: String, adType: String) {
        val bundle = Bundle().apply {
            putString("ad_unit_id", adUnitId)
            putString("ad_type", adType)
        }
        logEvent("ad_impression_custom", bundle)
    }
}

