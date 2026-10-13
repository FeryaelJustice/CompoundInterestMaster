/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.manager

import android.os.Bundle

/**
 * Interfaz de dominio para el registro de eventos y métricas de analítica.
 * Desacopla la lógica de negocio y presentación de la plataforma concreta (Firebase, etc.).
 */
interface AnalyticsManager {
    /**
     * Registra un evento de analítica con parámetros opcionales.
     */
    fun logEvent(name: String, params: Bundle? = null)

    /**
     * Registra el evento de cálculo de interés con sus propiedades clave.
     */
    fun logCalculationPerformed(
        calculationType: String,
        initialCapital: Double,
        annualRate: Double,
        years: Int,
        currencyCode: String
    )

    /**
     * Registra el cambio de tipo de cálculo.
     */
    fun logCalculationTypeSelected(calculationType: String)


    /**
     * Registra el cambio de divisa seleccionada.
     */
    fun logCurrencySelected(currencyCode: String)

    /**
     * Registra la visualización de un anuncio intersticial.
     */
    fun logAdImpression(adUnitId: String, adType: String = "interstitial")
}

