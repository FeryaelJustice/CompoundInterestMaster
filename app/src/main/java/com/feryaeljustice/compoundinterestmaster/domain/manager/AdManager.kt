/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.manager

import android.app.Activity

/**
 * Interfaz de dominio para la gestion y presentacion de anuncios y consentimiento de usuario.
 * Desacopla la logica de negocio y presentacion de la implementacion de la red publicitaria y UMP.
 */
interface AdManager {
    /**
     * Inicializa el SDK de anuncios si el consentimiento lo permite.
     */
    fun initialize()

    /**
     * Solicita actualizacion de informacion de consentimiento mediante User Messaging Platform (UMP)
     * y presenta el formulario de consentimiento si es requerido por normativas de privacidad (GDPR, etc.).
     * Si se obtiene o ya existe consentimiento, inicializa MobileAds y precarga anuncios.
     *
     * @param activity Actividad necesaria para presentar los dialogos de consentimiento.
     * @param onConsentComplete Callback opcional ejecutado al finalizar el flujo de consentimiento.
     */
    fun requestConsentAndInitialize(
        activity: Activity,
        onConsentComplete: (() -> Unit)? = null
    )

    /**
     * Muestra el formulario de opciones de privacidad de UMP para permitir al usuario modificar sus elecciones de consentimiento.
     *
     * @param activity Actividad para presentar el formulario.
     * @param onDismissed Callback opcional al cerrar el formulario.
     */
    fun showPrivacyOptions(
        activity: Activity,
        onDismissed: (() -> Unit)? = null
    )

    /**
     * Determina si se requiere mostrar la opcion de configuracion de privacidad de UMP.
     */
    fun isPrivacyOptionsRequired(): Boolean

    /**
     * Indica si el estado de consentimiento actual permite realizar solicitudes de anuncios a AdMob.
     */
    fun canRequestAds(): Boolean

    /**
     * Restablece el estado de consentimiento de UMP (util para pruebas y testing de flujo inicial).
     */
    fun resetConsent()

    /**
     * Precarga un anuncio intersticial en segundo plano si el consentimiento lo permite.
     */
    fun loadInterstitial()

    /**
     * Muestra un anuncio intersticial si esta disponible y respeta las politicas de frecuencia.
     *
     * @param activity La actividad actual necesaria para presentar el anuncio a pantalla completa.
     * @param force Si es true, ignora el contador y temporizador de frecuencia.
     * @param onAdDismissed Callback ejecutado al cerrar el anuncio o si no se mostro.
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
