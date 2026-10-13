/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.manager

import android.app.Activity

/**
 * Interfaz de dominio para la gestión y presentación de anuncios.
 * Desacopla la lógica de negocio y presentación de la implementación de la red publicitaria.
 */
interface AdManager {
    /**
     * Inicializa el SDK de anuncios.
     */
    fun initialize()

    /**
     * Precarga un anuncio intersticial en segundo plano.
     */
    fun loadInterstitial()

    /**
     * Muestra un anuncio intersticial si está disponible y respeta las políticas de frecuencia.
     *
     * @param activity La actividad actual necesaria para presentar el anuncio a pantalla completa.
     * @param force Si es true, ignora el contador y temporizador de frecuencia.
     * @param onAdDismissed Callback ejecutado al cerrar el anuncio o si no se mostró.
     */
    fun showInterstitial(
        activity: Activity,
        force: Boolean = false,
        onAdDismissed: (() -> Unit)? = null
    )

    /**
     * Retorna si hay un anuncio intersticial listo para mostrar.
     */
    fun isInterstitialLoaded(): Boolean
}
