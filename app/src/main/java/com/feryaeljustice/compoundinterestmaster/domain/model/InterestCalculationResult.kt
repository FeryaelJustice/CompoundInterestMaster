/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.model

data class InterestCalculationResult(
    val initialCapital: Double,
    val totalContributions: Double,
    val totalInterest: Double,
    val finalValue: Double,
    val yearlyBreakdown: List<YearlyGrowth>
)

data class YearlyGrowth(
    val year: Int,
    val balance: Double,
    val totalInterest: Double,
    val totalContributions: Double
)