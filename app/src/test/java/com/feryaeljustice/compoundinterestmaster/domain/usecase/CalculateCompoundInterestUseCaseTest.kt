/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.usecase

import com.feryaeljustice.compoundinterestmaster.domain.model.CalculationType
import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CalculateCompoundInterestUseCaseTest {

    private lateinit var useCase: CalculateCompoundInterestUseCase

    @Before
    fun setUp() {
        val futureValueUseCase = CalculateFutureValueUseCase()
        val timeToGoalUseCase = CalculateTimeToGoalUseCase()
        val requiredContributionUseCase = CalculateRequiredContributionUseCase(futureValueUseCase)
        val requiredRateUseCase = CalculateRequiredRateUseCase(futureValueUseCase)

        useCase = CalculateCompoundInterestUseCase(
            calculateFutureValueUseCase = futureValueUseCase,
            calculateTimeToGoalUseCase = timeToGoalUseCase,
            calculateRequiredContributionUseCase = requiredContributionUseCase,
            calculateRequiredRateUseCase = requiredRateUseCase
        )
    }

    @Test
    fun `calculate with monthly compounding and contributions`() {
        val result = useCase(
            calculationType = CalculationType.FUTURE_VALUE,
            initialCapital = 10000.0,
            annualRate = 2.0,
            years = 2,
            targetAmount = 0.0,
            compoundingFrequency = CompoundingFrequency.MONTHLY,
            periodicContribution = 1200.0,
            contributionTiming = ContributionTiming.END
        )

        // Initial Capital: 10000
        // Monthly Rate: 0.02 / 12
        // Total Months: 24
        // Contribution: 1200
        assertEquals(39766.57, result.finalValue, 0.01)
        assertEquals(966.57, result.totalInterest, 0.01)
        assertEquals(28800.0, result.totalContributions, 0.01)
        assertEquals(2, result.yearlyBreakdown.size)
    }

    @Test
    fun `calculate with annual compounding and no contributions`() {
        val result = useCase(
            calculationType = CalculationType.FUTURE_VALUE,
            initialCapital = 1000.0,
            annualRate = 10.0,
            years = 1,
            targetAmount = 0.0,
            compoundingFrequency = CompoundingFrequency.ANNUALLY,
            periodicContribution = 0.0,
            contributionTiming = ContributionTiming.END
        )

        // (1000 + 0) * 1.1 = 1100
        assertEquals(1100.0, result.finalValue, 0.01)
        assertEquals(100.0, result.totalInterest, 0.01)
        assertEquals(0.0, result.totalContributions, 0.01)
    }

    @Test
    fun `calculate with weekly compounding`() {
        val result = useCase(
            calculationType = CalculationType.FUTURE_VALUE,
            initialCapital = 1000.0,
            annualRate = 4.0,
            years = 1,
            targetAmount = 0.0,
            compoundingFrequency = CompoundingFrequency.WEEKLY,
            periodicContribution = 0.0,
            contributionTiming = ContributionTiming.END
        )

        assertEquals(1040.78, result.finalValue, 0.1)
        assertTrue(result.totalInterest > 0)
    }
}