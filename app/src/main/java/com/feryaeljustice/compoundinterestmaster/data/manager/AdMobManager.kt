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
import com.feryaeljustice.compoundinterestmaster.domain.manager.CrashlyticsManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
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
    private val analyticsManager: AnalyticsManager,
    private val crashlyticsManager: CrashlyticsManager
) : AdManager {

    private val adScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val mainHandler = Handler(Looper.getMainLooper())

    private var interstitialAd: InterstitialAd? = null
    private val isLoading = AtomicBoolean(false)
    private val isMobileAdsInitialized = AtomicBoolean(false)

    private val interstitialAdUnitId = BuildConfig.ADMOB_INTERSTITIAL_AD_UNIT_ID

    // Control de frecuencia para cumplimiento de Better Ads Standards de Google Play
    private var calculationCounter = 0
    private var lastAdShowTimestamp = 0L

    companion object {
        private const val TAG = "AdMobManager"
        private const val MIN_COOLDOWN_MILLIS = 45_000L // 45 segundos minimos entre intersticiales
        private const val REQUIRED_ACTIONS_FOR_AD = 2   // Minimo de calculos antes de sugerir un anuncio
    }

    override fun initialize() {
        if (canRequestAds()) {
            initializeMobileAds()
        } else {
            Log.d(TAG, "AdMob initialize pospuesto: pendiente de consentimiento UMP.")
        }
    }

    override fun requestConsentAndInitialize(
        activity: Activity,
        onConsentComplete: (() -> Unit)?
    ) {
        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)

        val paramsBuilder = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)

        if (BuildConfig.DEBUG) {
            // En modo depuracion se forza la geografia EEA para que Google simule conexion europea y despliegue el formulario
            val debugSettings = ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .build()
            paramsBuilder.setConsentDebugSettings(debugSettings)
        }

        val params = paramsBuilder.build()

        Log.i(
            TAG,
            "UMP Iniciando solicitud de consentimiento. canRequestAds=${consentInformation.canRequestAds()}, consentStatus=${consentInformation.consentStatus}"
        )

        // Si ya contabamos con consentimiento otorgado de una sesion previa, inicializamos sin demora
        if (consentInformation.canRequestAds()) {
            initializeMobileAds()
        }

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                Log.i(
                    TAG,
                    "UMP Info actualizada: formAvailable=${consentInformation.isConsentFormAvailable}, consentStatus=${consentInformation.consentStatus}, requirementStatus=${consentInformation.privacyOptionsRequirementStatus}"
                )

                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    if (formError != null) {
                        Log.w(
                            TAG,
                            "UMP Error en formulario de consentimiento: Code=${formError.errorCode}, Msg=${formError.message}"
                        )
                        crashlyticsManager.log("UMP FormError: ${formError.errorCode}: ${formError.message}")
                    } else {
                        Log.i(
                            TAG,
                            "UMP Formulario completado o no requerido. canRequestAds=${consentInformation.canRequestAds()}"
                        )
                    }

                    if (consentInformation.canRequestAds()) {
                        initializeMobileAds()
                    }
                    onConsentComplete?.invoke()
                }
            },
            { requestConsentError ->
                Log.w(
                    TAG,
                    "UMP Error al actualizar info de consentimiento: Code=${requestConsentError.errorCode}, Msg=${requestConsentError.message}"
                )
                crashlyticsManager.log(
                    "UMP RequestError: ${requestConsentError.errorCode}: ${requestConsentError.message}"
                )
                if (consentInformation.canRequestAds()) {
                    initializeMobileAds()
                }
                onConsentComplete?.invoke()
            }
        )
    }

    override fun resetConsent() {
        val consentInformation = UserMessagingPlatform.getConsentInformation(context)
        consentInformation.reset()
        Log.i(TAG, "UMP Consentimiento reseteado correctamente para pruebas.")
    }

    private fun initializeMobileAds() {
        if (!isMobileAdsInitialized.compareAndSet(false, true)) {
            return
        }

        adScope.launch(Dispatchers.IO) {
            try {
                MobileAds.initialize(context) { status ->
                    Log.d(TAG, "AdMob inicializado correctamente: ${status.adapterStatusMap}")
                    crashlyticsManager.log("AdMob MobileAds initialized successfully")
                }
                mainHandler.post {
                    if (interstitialAdUnitId.isNotBlank()) {
                        loadInterstitial()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error inicializando AdMob MobileAds", e)
                crashlyticsManager.recordException(e)
            }
        }
    }

    override fun showPrivacyOptions(
        activity: Activity,
        onDismissed: (() -> Unit)?
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
            if (formError != null) {
                Log.w(
                    TAG,
                    "Error mostrando opciones de privacidad UMP: ${formError.errorCode} - ${formError.message}"
                )
                crashlyticsManager.log("UMP PrivacyOptions Error: ${formError.errorCode}: ${formError.message}")
            }
            onDismissed?.invoke()
        }
    }

    override fun isPrivacyOptionsRequired(): Boolean {
        val consentInformation = UserMessagingPlatform.getConsentInformation(context)
        return consentInformation.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
    }

    override fun canRequestAds(): Boolean {
        val consentInformation = UserMessagingPlatform.getConsentInformation(context)
        return consentInformation.canRequestAds()
    }

    override fun loadInterstitial() {
        if (!canRequestAds()) {
            Log.d(TAG, "Omitiendo carga de anuncio: consentimiento UMP aun no disponible.")
            return
        }

        if (interstitialAdUnitId.isBlank()) {
            Log.d(TAG, "AdMob Interstitial AdUnitId esta vacio. Omitiendo carga.")
            return
        }

        if (interstitialAd != null) {
            Log.d(TAG, "Ya existe un anuncio intersticial precargado.")
            return
        }

        if (!isLoading.compareAndSet(false, true)) {
            Log.d(TAG, "La carga del intersticial ya esta en curso.")
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
                        crashlyticsManager.log("Interstitial load failed: ${loadAdError.code} - ${loadAdError.message}")
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
            Log.w(TAG, "La actividad ya no esta en estado valido para mostrar el anuncio.")
            onAdDismissed?.invoke()
            return
        }

        val currentTime = System.currentTimeMillis()
        if (!force) {
            calculationCounter++
            val timeSinceLastAd = currentTime - lastAdShowTimestamp
            if (calculationCounter < REQUIRED_ACTIONS_FOR_AD || timeSinceLastAd < MIN_COOLDOWN_MILLIS) {
                Log.d(
                    TAG,
                    "Frecuencia controlada: calculos=$calculationCounter, tiempo transcurrido=${timeSinceLastAd}ms. Omitiendo anuncio."
                )
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
                    crashlyticsManager.log("Interstitial show failed: ${adError.code} - ${adError.message}")
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
            Log.d(TAG, "Intersticial no estaba listo al solicitar mostrar. Cargando para la proxima.")
            loadInterstitial()
            onAdDismissed?.invoke()
        }
    }

    override fun isInterstitialLoaded(): Boolean {
        return interstitialAd != null
    }
}
