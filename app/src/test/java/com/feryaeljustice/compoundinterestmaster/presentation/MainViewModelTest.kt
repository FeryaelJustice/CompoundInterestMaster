/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import com.feryaeljustice.compoundinterestmaster.data.mapper.LocaleMapper
import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.usecase.CalculateCompoundInterestUseCase
import com.feryaeljustice.compoundinterestmaster.domain.usecase.CalculateFutureValueUseCase
import com.feryaeljustice.compoundinterestmaster.domain.usecase.CalculateRequiredContributionUseCase
import com.feryaeljustice.compoundinterestmaster.domain.usecase.CalculateRequiredRateUseCase
import com.feryaeljustice.compoundinterestmaster.domain.usecase.CalculateTimeToGoalUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var localeMapper: LocaleMapper
    private lateinit var useCase: CalculateCompoundInterestUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        localeMapper = LocaleMapper()
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
        val fakeAnalyticsManager = object : com.feryaeljustice.compoundinterestmaster.domain.manager.AnalyticsManager {
            override fun logEvent(name: String, params: android.os.Bundle?) {}
            override fun logCalculationPerformed(
                calculationType: String,
                initialCapital: Double,
                annualRate: Double,
                years: Int,
                currencyCode: String
            ) {}
            override fun logCalculationTypeSelected(calculationType: String) {}
            override fun logCurrencySelected(currencyCode: String) {}
            override fun logAdImpression(adUnitId: String, adType: String) {}
        }

        val fakeCrashlyticsManager = object : com.feryaeljustice.compoundinterestmaster.domain.manager.CrashlyticsManager {
            override fun recordException(throwable: Throwable) {}
            override fun log(message: String) {}
            override fun setCustomKey(key: String, value: String) {}
            override fun setCustomKey(key: String, value: Boolean) {}
            override fun setCustomKey(key: String, value: Double) {}
            override fun setCustomKey(key: String, value: Int) {}
            override fun setUserId(userId: String) {}
        }

        viewModel = MainViewModel(localeMapper, useCase, fakeAnalyticsManager, fakeCrashlyticsManager)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should match MainUiState defaults and trigger calculation`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("10000", state.initialCapital)
        assertEquals("2", state.annualRate)
        assertEquals("2", state.years)
        assertEquals(39766.57, state.result?.finalValue ?: 0.0, 0.01)
    }

    @Test
    fun `updating initial capital updates uiState`() = runTest {
        viewModel.onInitialCapitalChange("5000")
        assertEquals("5000", viewModel.uiState.value.initialCapital)
    }

    @Test
    fun `calculate correctly updates result for custom values`() = runTest {
        viewModel.onInitialCapitalChange("1000")
        viewModel.onAnnualRateChange("10")
        viewModel.onYearsChange("1")
        viewModel.onCompoundingFrequencyChange(CompoundingFrequency.ANNUALLY)
        viewModel.onPeriodicContributionChange("0")

        viewModel.calculate()
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.uiState.value.result
        assertEquals(1100.0, result?.finalValue ?: 0.0, 0.01)
        assertEquals(100.0, result?.totalInterest ?: 0.0, 0.01)
    }
}
