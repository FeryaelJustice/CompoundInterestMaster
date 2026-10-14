/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.manager

/**
 * Interfaz de dominio para el registro de errores, no fatales y métricas de diagnóstico.
 * Desacopla la lógica de la aplicación del SDK directo de Firebase Crashlytics.
 */
interface CrashlyticsManager {
    /**
     * Registra una excepción no fatal para su análisis en Firebase Console.
     */
    fun recordException(throwable: Throwable)

    /**
     * Registra un mensaje de rastro (breadcrumb) que acompaña a futuros informes de crash.
     */
    fun log(message: String)

    /**
     * Asocia una clave personalizada de tipo String al reporte de diagnóstico.
     */
    fun setCustomKey(key: String, value: String)

    /**
     * Asocia una clave personalizada de tipo Boolean al reporte de diagnóstico.
     */
    fun setCustomKey(key: String, value: Boolean)

    /**
     * Asocia una clave personalizada de tipo Double al reporte de diagnóstico.
     */
    fun setCustomKey(key: String, value: Double)

    /**
     * Asocia una clave personalizada de tipo Int al reporte de diagnóstico.
     */
    fun setCustomKey(key: String, value: Int)

    /**
     * Configura el identificador anónimo de usuario para correlacionar reportes.
     */
    fun setUserId(userId: String)
}
