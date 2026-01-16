/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.compoundinterestmaster.data.mapper.LocaleMapper
import com.feryaeljustice.compoundinterestmaster.domain.model.CalculationType
import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming
import com.feryaeljustice.compoundinterestmaster.domain.usecase.CalculateCompoundInterestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localeMapper: LocaleMapper,
    private val calculateCompoundInterestUseCase: CalculateCompoundInterestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCurrencies()
        calculate()
    }

    fun loadCurrencies(){
        viewModelScope.launch {
            val currencies = localeMapper.getCurrencies()
            _uiState.update { it.copy(currencies = currencies) }
        }
    }

    fun onCalculationTypeChange(value: CalculationType) {
        _uiState.update { it.copy(calculationType = value) }
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

    fun onTargetAmountChange(value: String) {
        _uiState.update { it.copy(targetAmount = value) }
    }

    fun onCompoundingFrequencyChange(value: CompoundingFrequency) {
        _uiState.update { it.copy(compoundingFrequency = value) }
    }

    fun onPeriodicContributionChange(value: String) {
        _uiState.update { it.copy(periodicContribution = value) }
    }

    fun onContributionTimingChange(value: ContributionTiming) {
        _uiState.update { it.copy(contributionTiming = value) }
    }

    fun calculate() {
        val state = _uiState.value
        val initialCapital = state.initialCapital.toDoubleOrNull() ?: 0.0
        val annualRate = state.annualRate.toDoubleOrNull() ?: 0.0
        val years = state.years.toIntOrNull() ?: 0
        val targetAmount = state.targetAmount.toDoubleOrNull() ?: 0.0
        val periodicContribution = state.periodicContribution.toDoubleOrNull() ?: 0.0

        val result = calculateCompoundInterestUseCase(
            calculationType = state.calculationType,
            initialCapital = initialCapital,
            annualRate = annualRate,
            years = years,
            targetAmount = targetAmount,
            compoundingFrequency = state.compoundingFrequency,
            periodicContribution = periodicContribution,
            contributionTiming = state.contributionTiming
        )

        _uiState.update { it.copy(result = result) }
    }
}
