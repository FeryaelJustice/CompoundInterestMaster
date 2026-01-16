/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import com.feryaeljustice.compoundinterestmaster.domain.model.CICurrency
import com.feryaeljustice.compoundinterestmaster.domain.model.CalculationType
import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming
import com.feryaeljustice.compoundinterestmaster.domain.model.InterestCalculationResult

data class MainUiState(
    val calculationType: CalculationType = CalculationType.FUTURE_VALUE,
    val initialCapital: String = "10000",
    val annualRate: String = "2",
    val years: String = "2",
    val targetAmount: String = "50000",
    val compoundingFrequency: CompoundingFrequency = CompoundingFrequency.MONTHLY,
    val periodicContribution: String = "1200",
    val contributionTiming: ContributionTiming = ContributionTiming.END,
    val result: InterestCalculationResult? = null,
    val currencies: List<CICurrency> = emptyList()
)
