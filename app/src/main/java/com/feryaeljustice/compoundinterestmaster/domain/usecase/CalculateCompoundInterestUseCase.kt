/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.usecase

import com.feryaeljustice.compoundinterestmaster.domain.model.CalculationType
import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming
import com.feryaeljustice.compoundinterestmaster.domain.model.InterestCalculationResult
import javax.inject.Inject

class CalculateCompoundInterestUseCase @Inject constructor(
    private val calculateFutureValueUseCase: CalculateFutureValueUseCase,
    private val calculateTimeToGoalUseCase: CalculateTimeToGoalUseCase,
    private val calculateRequiredContributionUseCase: CalculateRequiredContributionUseCase,
    private val calculateRequiredRateUseCase: CalculateRequiredRateUseCase
) {

    operator fun invoke(
        calculationType: CalculationType,
        initialCapital: Double,
        annualRate: Double,
        years: Int,
        targetAmount: Double,
        compoundingFrequency: CompoundingFrequency,
        periodicContribution: Double,
        contributionTiming: ContributionTiming
    ): InterestCalculationResult {
        return when (calculationType) {
            CalculationType.FUTURE_VALUE -> {
                calculateFutureValueUseCase(
                    initialCapital = initialCapital,
                    annualRate = annualRate,
                    years = years,
                    compoundingFrequency = compoundingFrequency,
                    periodicContribution = periodicContribution,
                    contributionTiming = contributionTiming
                )
            }
            CalculationType.TIME_TO_GOAL -> {
                calculateTimeToGoalUseCase(
                    initialCapital = initialCapital,
                    targetAmount = targetAmount,
                    annualRate = annualRate,
                    compoundingFrequency = compoundingFrequency,
                    periodicContribution = periodicContribution,
                    contributionTiming = contributionTiming
                )
            }
            CalculationType.REQUIRED_CONTRIBUTION -> {
                calculateRequiredContributionUseCase(
                    initialCapital = initialCapital,
                    targetAmount = targetAmount,
                    annualRate = annualRate,
                    years = years,
                    compoundingFrequency = compoundingFrequency,
                    contributionTiming = contributionTiming
                )
            }
            CalculationType.REQUIRED_RATE -> {
                calculateRequiredRateUseCase(
                    initialCapital = initialCapital,
                    targetAmount = targetAmount,
                    years = years,
                    compoundingFrequency = compoundingFrequency,
                    periodicContribution = periodicContribution,
                    contributionTiming = contributionTiming
                )
            }
        }
    }
}
