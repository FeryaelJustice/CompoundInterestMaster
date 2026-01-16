/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.usecase

import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming
import com.feryaeljustice.compoundinterestmaster.domain.model.InterestCalculationResult
import javax.inject.Inject
import kotlin.math.pow

class CalculateRequiredContributionUseCase @Inject constructor(
    private val calculateFutureValueUseCase: CalculateFutureValueUseCase
) {

    operator fun invoke(
        initialCapital: Double,
        targetAmount: Double,
        annualRate: Double,
        years: Int,
        compoundingFrequency: CompoundingFrequency,
        contributionTiming: ContributionTiming
    ): InterestCalculationResult {
        val r = annualRate / 100.0
        val n = compoundingFrequency.timesPerYear.toDouble()
        val t = years.toDouble()
        
        // n total de meses (nuestra unidad de aportación)
        val totalMonths = t * 12.0
        
        // Tasa efectiva mensual equivalente para que A = P(1+r/n)^(nt)
        val monthlyRate = (1 + r / n).pow(n / 12.0) - 1

        // Valor futuro del capital inicial tras t años
        val fvPrincipal = initialCapital * (1 + r / n).pow(n * t)
        val neededFromContributions = targetAmount - fvPrincipal

        val requiredMonthlyContribution = if (neededFromContributions <= 0) {
            0.0
        } else {
            if (monthlyRate == 0.0) {
                neededFromContributions / totalMonths
            } else {
                // Fórmula de anualidad para encontrar la aportación periódica (PMT)
                // PMT = FV / [ ((1+i)^m - 1) / i ] * (1+i)^k
                // k=1 si es al inicio, k=0 si es al final
                val factor = ((1 + monthlyRate).pow(totalMonths) - 1) / monthlyRate
                if (contributionTiming == ContributionTiming.BEGINNING) {
                    neededFromContributions / (factor * (1 + monthlyRate))
                } else {
                    neededFromContributions / factor
                }
            }
        }

        return calculateFutureValueUseCase(
            initialCapital = initialCapital,
            annualRate = annualRate,
            years = years,
            compoundingFrequency = compoundingFrequency,
            periodicContribution = requiredMonthlyContribution,
            contributionTiming = contributionTiming
        )
    }
}
