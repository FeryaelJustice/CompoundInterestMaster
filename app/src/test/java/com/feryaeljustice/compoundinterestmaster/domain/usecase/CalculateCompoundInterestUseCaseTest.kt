/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.usecase

import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CalculateCompoundInterestUseCaseTest {

    private lateinit var useCase: CalculateCompoundInterestUseCase

    @Before
    fun setUp() {
        useCase = CalculateCompoundInterestUseCase()
    }

    @Test
    fun `calculate with monthly compounding and contributions`() {
        val result = useCase(
            initialCapital = 10000.0,
            annualRate = 2.0,
            years = 2,
            compoundingFrequency = CompoundingFrequency.MONTHLY,
            monthlyContribution = 1200.0
        )

        // Initial Capital: 10000
        // Monthly Rate: 0.02 / 12 = 0.001666...
        // Total Months: 24
        // Contribution: 1200
        
        // Manual verification approx:
        // After 1 month: (10000 + 1200) * 1.00166... = 11218.67
        // ...
        
        // Using the same values from Task 5 verification which were €39,766.57
        assertEquals(39766.57, result.finalValue, 0.01)
        assertEquals(966.57, result.totalInterest, 0.01)
        assertEquals(28800.0, result.totalContributions, 0.01)
        assertEquals(2, result.yearlyBreakdown.size)
    }

    @Test
    fun `calculate with annual compounding and no contributions`() {
        val result = useCase(
            initialCapital = 1000.0,
            annualRate = 10.0,
            years = 1,
            compoundingFrequency = CompoundingFrequency.ANNUALLY,
            monthlyContribution = 0.0
        )

        // (1000 + 0) * 1.1 = 1100
        assertEquals(1100.0, result.finalValue, 0.01)
        assertEquals(100.0, result.totalInterest, 0.01)
        assertEquals(0.0, result.totalContributions, 0.01)
    }

    @Test
    fun `calculate with quarterly compounding`() {
        val result = useCase(
            initialCapital = 1000.0,
            annualRate = 4.0,
            years = 1,
            compoundingFrequency = CompoundingFrequency.QUARTERLY,
            monthlyContribution = 0.0
        )

        // Rate per period: 0.04 / 4 = 0.01
        // Periods: 4
        // 1000 * (1.01)^4 = 1040.60
        assertEquals(1040.60, result.finalValue, 0.01)
        assertEquals(40.60, result.totalInterest, 0.01)
    }
}