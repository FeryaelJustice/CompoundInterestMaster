/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.model

enum class CompoundingFrequency(val timesPerYear: Int) {
    ANNUALLY(1),
    SEMI_ANNUALLY(2),
    QUARTERLY(4),
    MONTHLY(12),
    DAILY(365)
}