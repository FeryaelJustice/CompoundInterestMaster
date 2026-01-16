/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.usecase

import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming
import com.feryaeljustice.compoundinterestmaster.domain.model.InterestCalculationResult
import com.feryaeljustice.compoundinterestmaster.domain.model.YearlyGrowth
import javax.inject.Inject

class CalculateTimeToGoalUseCase @Inject constructor() {

    operator fun invoke(
        initialCapital: Double,
        targetAmount: Double,
        annualRate: Double,
        compoundingFrequency: CompoundingFrequency,
        periodicContribution: Double,
        contributionTiming: ContributionTiming
    ): InterestCalculationResult {
        val yearlyBreakdown = mutableListOf<YearlyGrowth>()
        var currentBalance = initialCapital
        var currentBalanceWithoutInterest = initialCapital
        var totalContributions = 0.0
        var years = 0
        
        val r = annualRate / 100.0
        val n = compoundingFrequency.timesPerYear
        val periodicRate = if (n > 0) r / n.toDouble() else 0.0

        if (currentBalance >= targetAmount) {
            return InterestCalculationResult(initialCapital, 0.0, 0.0, currentBalance, initialCapital, emptyList())
        }

        while (currentBalance < targetAmount && years < 100) {
            years++
            for (p in 1..n) {
                if (contributionTiming == ContributionTiming.BEGINNING) {
                    currentBalance += periodicContribution
                    currentBalanceWithoutInterest += periodicContribution
                    totalContributions += periodicContribution
                }

                currentBalance *= (1 + periodicRate)

                if (contributionTiming == ContributionTiming.END) {
                    currentBalance += periodicContribution
                    currentBalanceWithoutInterest += periodicContribution
                    totalContributions += periodicContribution
                }
                
                if (currentBalance >= targetAmount) break
            }
            
            yearlyBreakdown.add(
                YearlyGrowth(
                    year = years,
                    balance = currentBalance,
                    balanceWithoutInterest = currentBalanceWithoutInterest,
                    totalInterest = currentBalance - currentBalanceWithoutInterest,
                    totalContributions = totalContributions
                )
            )
            if (currentBalance >= targetAmount) break
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
