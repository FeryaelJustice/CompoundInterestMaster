/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.usecase

import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.InterestCalculationResult
import com.feryaeljustice.compoundinterestmaster.domain.model.YearlyGrowth
import javax.inject.Inject
import kotlin.math.pow

class CalculateCompoundInterestUseCase @Inject constructor() {

    operator fun invoke(
        initialCapital: Double,
        annualRate: Double,
        years: Int,
        compoundingFrequency: CompoundingFrequency,
        monthlyContribution: Double
    ): InterestCalculationResult {
        val yearlyBreakdown = mutableListOf<YearlyGrowth>()
        var currentBalance = initialCapital
        var totalContributions = 0.0
        var totalInterestEarned = 0.0

        val rateDecimal = annualRate / 100.0
        val monthsPerCompounding = 12 / compoundingFrequency.timesPerYear.coerceAtMost(12)

        for (year in 1..years) {
            for (month in 1..12) {
                // If compounding happens this month (at the end of the month)
                if (compoundingFrequency.timesPerYear >= 12) {
                    // Compounding monthly or more frequent
                    val ratePerMonth = rateDecimal / 12.0
                    val interest = currentBalance * ratePerMonth
                    currentBalance += interest
                    totalInterestEarned += interest
                } else {
                    // Compounding less than monthly (e.g., quarterly, annually)
                    if (month % monthsPerCompounding == 0) {
                        val ratePerPeriod = rateDecimal / compoundingFrequency.timesPerYear
                        val interest = currentBalance * ratePerPeriod
                        currentBalance += interest
                        totalInterestEarned += interest
                    }
                }
                
                // Contribution at end of month (after interest)
                currentBalance += monthlyContribution
                totalContributions += monthlyContribution
            }
            yearlyBreakdown.add(
                YearlyGrowth(
                    year = year,
                    balance = currentBalance,
                    totalInterest = totalInterestEarned,
                    totalContributions = totalContributions
                )
            )
        }

        return InterestCalculationResult(
            initialCapital = initialCapital,
            totalContributions = totalContributions,
            totalInterest = totalInterestEarned,
            finalValue = currentBalance,
            yearlyBreakdown = yearlyBreakdown
        )
    }
}