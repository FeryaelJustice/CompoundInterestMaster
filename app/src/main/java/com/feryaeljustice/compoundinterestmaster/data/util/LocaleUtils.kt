/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.data.util

import java.util.Currency
import java.util.Date
import java.util.Locale

fun getAllAvailableLocales(date: Date = Date()): List<Currency> {
    return Locale.getAvailableLocales()
        .asSequence()
        .flatMap { locale -> android.icu.util.Currency.getAvailableCurrencyCodes(locale, date).orEmpty().toList() }
        .distinct()
        .map(Currency::getInstance)
        .toList()
}