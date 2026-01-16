/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class InterestCalculationResult(
    val initialCapital: Double,
    val totalContributions: Double,
    val totalInterest: Double,
    val finalValue: Double,
    val finalValueWithoutInterest: Double,
    val yearlyBreakdown: List<YearlyGrowth>
)

@Immutable
data class YearlyGrowth(
    val year: Int,
    val balance: Double,
    val balanceWithoutInterest: Double,
    val totalInterest: Double,
    val totalContributions: Double
)
