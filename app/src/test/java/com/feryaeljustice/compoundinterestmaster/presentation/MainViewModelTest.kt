/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.usecase.CalculateCompoundInterestUseCase
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
    private lateinit var useCase: CalculateCompoundInterestUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = CalculateCompoundInterestUseCase()
        viewModel = MainViewModel(useCase)
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
        // Verify initial calculation result (approximate values for 10k at 2% for 2 years with 1200 monthly)
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
        viewModel.onMonthlyContributionChange("0")
        
        viewModel.calculate()
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.uiState.value.result
        assertEquals(1100.0, result?.finalValue ?: 0.0, 0.01)
        assertEquals(100.0, result?.totalInterest ?: 0.0, 0.01)
    }
}
