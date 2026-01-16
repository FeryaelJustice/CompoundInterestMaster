/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.InterestCalculationResult

data class MainUiState(
    val initialCapital: String = "10000",
    val annualRate: String = "2",
    val years: String = "2",
    val compoundingFrequency: CompoundingFrequency = CompoundingFrequency.MONTHLY,
    val monthlyContribution: String = "1200",
    val result: InterestCalculationResult? = null
)