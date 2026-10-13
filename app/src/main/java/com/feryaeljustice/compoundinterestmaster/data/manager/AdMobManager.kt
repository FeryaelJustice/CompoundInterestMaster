/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.data.manager

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.feryaeljustice.compoundinterestmaster.BuildConfig
import com.feryaeljustice.compoundinterestmaster.domain.manager.AdManager
import com.feryaeljustice.compoundinterestmaster.domain.manager.AnalyticsManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdMobManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val analyticsManager: AnalyticsManager
) : AdManager {

    private val adScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val mainHandler = Handler(Looper.getMainLooper())


    private var interstitialAd: InterstitialAd? = null
    private val isLoading = AtomicBoolean(false)

    private val interstitialAdUnitId = BuildConfig.ADMOB_INTERSTITIAL_AD_UNIT_ID

    // Control de frecuencia para cumplimiento de Better Ads Standards de Google Play
    private var calculationCounter = 0
    private var lastAdShowTimestamp = 0L

    companion object {
        private const val TAG = "AdMobManager"
        private const val MIN_COOLDOWN_MILLIS = 45_000L // 45 segundos mínimos entre intersticiales
        private const val REQUIRED_ACTIONS_FOR_AD = 2   // Mínimo de cálculos antes de sugerir un anuncio
    }

    override fun initialize() {
        adScope.launch(Dispatchers.IO) {
            try {
                MobileAds.initialize(context) { status ->
                    Log.d(TAG, "AdMob inicializado correctamente: ${status.adapterStatusMap}")
                }
                mainHandler.post {
                    if (interstitialAdUnitId.isNotBlank()) {
                        loadInterstitial()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error inicializando AdMob MobileAds", e)
            }
        }
    }

    override fun loadInterstitial() {
        if (interstitialAdUnitId.isBlank()) {
            Log.d(TAG, "AdMob Interstitial AdUnitId está vacío. Omitiendo carga.")
            return
        }

        if (interstitialAd != null) {
            Log.d(TAG, "Ya existe un anuncio intersticial precargado.")
            return
        }

        if (!isLoading.compareAndSet(false, true)) {
            Log.d(TAG, "La carga del intersticial ya está en curso.")
            return
        }

        mainHandler.post {
            val adRequest = AdRequest.Builder().build()
            Log.d(TAG, "Iniciando carga de intersticial con AdUnit: $interstitialAdUnitId")

            InterstitialAd.load(
                context,
                interstitialAdUnitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        Log.d(TAG, "Intersticial cargado exitosamente.")
                        interstitialAd = ad
                        isLoading.set(false)
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.w(TAG, "Fallo al cargar intersticial: ${loadAdError.message} (Code: ${loadAdError.code})")
                        interstitialAd = null
                        isLoading.set(false)
                    }
                }
            )
        }
    }

    override fun showInterstitial(
        activity: Activity,
        force: Boolean,
        onAdDismissed: (() -> Unit)?
    ) {
        if (activity.isFinishing || activity.isDestroyed) {
            Log.w(TAG, "La actividad ya no está en estado válido para mostrar el anuncio.")
            onAdDismissed?.invoke()
            return
        }

        val currentTime = System.currentTimeMillis()
        if (!force) {
            calculationCounter++
            val timeSinceLastAd = currentTime - lastAdShowTimestamp
            if (calculationCounter < REQUIRED_ACTIONS_FOR_AD || timeSinceLastAd < MIN_COOLDOWN_MILLIS) {
                Log.d(TAG, "Frecuencia controlada: cálculos=$calculationCounter, tiempo transcurrido=${timeSinceLastAd}ms. Omitiendo anuncio.")
                onAdDismissed?.invoke()
                return
            }
        }

        val currentAd = interstitialAd
        if (currentAd != null) {
            currentAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Anuncio intersticial cerrado por el usuario.")
                    interstitialAd = null
                    lastAdShowTimestamp = System.currentTimeMillis()
                    calculationCounter = 0
                    onAdDismissed?.invoke()
                    loadInterstitial() // Precargar el siguiente
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.w(TAG, "Fallo al mostrar intersticial: ${adError.message}")
                    interstitialAd = null
                    onAdDismissed?.invoke()
                    loadInterstitial()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Intersticial mostrado en pantalla completa.")
                    interstitialAd = null
                    analyticsManager.logAdImpression(interstitialAdUnitId, "interstitial")
                }
            }


            mainHandler.post {
                currentAd.show(activity)
            }
        } else {
            Log.d(TAG, "Intersticial no estaba listo al solicitar mostrar. Cargando para la próxima.")
            loadInterstitial()
            onAdDismissed?.invoke()
        }
    }

    override fun isInterstitialLoaded(): Boolean {
        return interstitialAd != null
    }
}
