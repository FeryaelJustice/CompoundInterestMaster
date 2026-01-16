/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import androidx.lifecycle.ViewModel
import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.usecase.CalculateCompoundInterestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val calculateCompoundInterestUseCase: CalculateCompoundInterestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        calculate()
    }

    fun onInitialCapitalChange(value: String) {
        _uiState.update { it.copy(initialCapital = value) }
    }

    fun onAnnualRateChange(value: String) {
        _uiState.update { it.copy(annualRate = value) }
    }

    fun onYearsChange(value: String) {
        _uiState.update { it.copy(years = value) }
    }

    fun onCompoundingFrequencyChange(value: CompoundingFrequency) {
        _uiState.update { it.copy(compoundingFrequency = value) }
    }

    fun onMonthlyContributionChange(value: String) {
        _uiState.update { it.copy(monthlyContribution = value) }
    }

    fun calculate() {
        val state = _uiState.value
        val initialCapital = state.initialCapital.toDoubleOrNull() ?: 0.0
        val annualRate = state.annualRate.toDoubleOrNull() ?: 0.0
        val years = state.years.toIntOrNull() ?: 0
        val monthlyContribution = state.monthlyContribution.toDoubleOrNull() ?: 0.0

        val result = calculateCompoundInterestUseCase(
            initialCapital = initialCapital,
            annualRate = annualRate,
            years = years,
            compoundingFrequency = state.compoundingFrequency,
            monthlyContribution = monthlyContribution
        )

        _uiState.update { it.copy(result = result) }
    }
}