/*
 * Author: Feryael Justice
 * Date: 19/02/2025
 */

package com.feryaeljustice.compoundinterestmaster.domain.usecase

import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming
import com.feryaeljustice.compoundinterestmaster.domain.model.InterestCalculationResult
import com.feryaeljustice.compoundinterestmaster.domain.model.YearlyGrowth
import javax.inject.Inject

/**
 * Calcula el valor futuro (A) aplicando la fórmula de interés compuesto:
 * A = P(1 + r/n)^(nt)
 * 
 * Se integra la aportación periódica sumándola en cada periodo de capitalización 'n'.
 * Esto garantiza que la frecuencia de ahorro coincida con la frecuencia de capitalización,
 * permitiendo ver grandes diferencias en los totales según la frecuencia seleccionada (Anual, Mensual, Semanal).
 */
class CalculateFutureValueUseCase @Inject constructor() {

    operator fun invoke(
        initialCapital: Double,
        annualRate: Double,
        years: Int,
        compoundingFrequency: CompoundingFrequency,
        periodicContribution: Double,
        contributionTiming: ContributionTiming
    ): InterestCalculationResult {
        val yearlyBreakdown = mutableListOf<YearlyGrowth>()
        var currentBalance = initialCapital
        var currentBalanceWithoutInterest = initialCapital
        var totalContributions = 0.0

        val r = annualRate / 100.0
        val n = compoundingFrequency.timesPerYear
        val periodicRate = if (n > 0) r / n.toDouble() else 0.0

        for (year in 1..years) {
            for (p in 1..n) {
                // 1. Aportación al inicio del periodo
                if (contributionTiming == ContributionTiming.BEGINNING) {
                    currentBalance += periodicContribution
                    currentBalanceWithoutInterest += periodicContribution
                    totalContributions += periodicContribution
                }

                // 2. Aplicación del interés compuesto (r/n) sobre el balance acumulado
                currentBalance *= (1 + periodicRate)

                // 3. Aportación al final del periodo
                if (contributionTiming == ContributionTiming.END) {
                    currentBalance += periodicContribution
                    currentBalanceWithoutInterest += periodicContribution
                    totalContributions += periodicContribution
                }
            }

            yearlyBreakdown.add(
                YearlyGrowth(
                    year = year,
                    balance = currentBalance,
                    balanceWithoutInterest = currentBalanceWithoutInterest,
                    totalInterest = currentBalance - currentBalanceWithoutInterest,
                    totalContributions = totalContributions
                )
            )
        }

        return InterestCalculationResult(
            initialCapital = initialCapital,
            totalContributions = totalContributions,
            totalInterest = currentBalance - currentBalanceWithoutInterest,
            finalValue = currentBalance,
            finalValueWithoutInterest = currentBalanceWithoutInterest,
            yearlyBreakdown = yearlyBreakdown
        )
    }
}
