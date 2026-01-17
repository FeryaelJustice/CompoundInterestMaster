/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.data.mapper

import com.feryaeljustice.compoundinterestmaster.data.util.getAllAvailableLocales
import com.feryaeljustice.compoundinterestmaster.domain.model.CICurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton Mapper para gestionar las divisas disponibles.
 * Se utiliza para recuperar y cachear las divisas una sola vez al inicio.
 */
@Singleton
class LocaleMapper @Inject constructor() {
    private var cachedCurrencies: List<CICurrency>? = null

    /**
     * Recupera la lista de divisas únicas disponibles en el sistema.
     * Ejecuta la operación en un hilo de IO y cachea el resultado.
     */
    suspend fun getCurrencies(): List<CICurrency> = withContext(Dispatchers.IO) {
        cachedCurrencies ?: getAllAvailableLocales()
            .map { it.toCICurrency() }
            .distinctBy { it.currencyCode }
            .sortedBy { it.curName }
            .also { cachedCurrencies = it }
    }

    private fun Currency.toCICurrency(): CICurrency = CICurrency(
        curName = displayName,
        curSymbol = symbol,
        currencyCode = currencyCode
    )
}
