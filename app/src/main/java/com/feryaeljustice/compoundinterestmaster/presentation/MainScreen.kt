/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.compoundinterestmaster.R
import com.feryaeljustice.compoundinterestmaster.domain.model.CICurrency
import com.feryaeljustice.compoundinterestmaster.domain.model.CalculationType
import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming
import com.feryaeljustice.compoundinterestmaster.domain.model.InterestCalculationResult
import com.feryaeljustice.compoundinterestmaster.ui.components.CalculationTypeSelector
import com.feryaeljustice.compoundinterestmaster.ui.components.CurrencyInputField
import com.feryaeljustice.compoundinterestmaster.ui.components.CurrencySelector
import com.feryaeljustice.compoundinterestmaster.ui.components.DetailedBreakdownCard
import com.feryaeljustice.compoundinterestmaster.ui.components.FrequencyDropdown
import com.feryaeljustice.compoundinterestmaster.ui.components.InterestChart
import com.feryaeljustice.compoundinterestmaster.ui.components.PercentageInputField
import com.feryaeljustice.compoundinterestmaster.ui.components.ResultValueCard
import com.feryaeljustice.compoundinterestmaster.ui.components.TimingSelector
import com.feryaeljustice.compoundinterestmaster.ui.components.YearsInputField
import com.feryaeljustice.compoundinterestmaster.ui.theme.CompoundInterestMasterTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection()

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Form Section
                InputForm(
                    uiState = uiState,
                    onCalculationTypeChange = viewModel::onCalculationTypeChange,
                    onCurrencyChange = viewModel::onCurrencyChange,
                    onInitialCapitalChange = viewModel::onInitialCapitalChange,
                    onAnnualRateChange = viewModel::onAnnualRateChange,
                    onYearsChange = viewModel::onYearsChange,
                    onTargetAmountChange = viewModel::onTargetAmountChange,
                    onFrequencyChange = viewModel::onCompoundingFrequencyChange,
                    onPeriodicContributionChange = viewModel::onPeriodicContributionChange,
                    onTimingChange = viewModel::onContributionTimingChange,
                    onCalculate = viewModel::calculate
                )

                // Animated Results Section
                AnimatedVisibility(
                    visible = uiState.result != null,
                    enter = fadeIn() + expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    uiState.result?.let { res ->
                        ResultsSection(
                            result = res,
                            calculationType = uiState.calculationType,
                            years = uiState.years,
                            currencySymbol = uiState.selectedCurrency.curSymbol
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun HeaderSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF3F51B5),
                        Color(0xFFE91E63)
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(vertical = 48.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.calc_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.calc_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InputForm(
    uiState: MainUiState,
    onCalculationTypeChange: (CalculationType) -> Unit,
    onCurrencyChange: (CICurrency) -> Unit,
    onInitialCapitalChange: (String) -> Unit,
    onAnnualRateChange: (String) -> Unit,
    onYearsChange: (String) -> Unit,
    onTargetAmountChange: (String) -> Unit,
    onFrequencyChange: (CompoundingFrequency) -> Unit,
    onPeriodicContributionChange: (String) -> Unit,
    onTimingChange: (ContributionTiming) -> Unit,
    onCalculate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalculationTypeSelector(
                selectedType = uiState.calculationType,
                onTypeChange = onCalculationTypeChange
            )

            CurrencySelector(
                selectedCurrency = uiState.selectedCurrency,
                currencies = uiState.currencies,
                onCurrencyChange = onCurrencyChange
            )

            CurrencyInputField(
                value = uiState.initialCapital,
                onValueChange = onInitialCapitalChange,
                label = stringResource(R.string.label_initial_capital, uiState.selectedCurrency.curSymbol),
                testTag = "initial_capital_input"
            )

            // Mostrar Objetivo de Ahorro si no es FUTURE_VALUE
            if (uiState.calculationType != CalculationType.FUTURE_VALUE) {
                CurrencyInputField(
                    value = uiState.targetAmount,
                    onValueChange = onTargetAmountChange,
                    label = stringResource(R.string.label_target_amount, uiState.selectedCurrency.curSymbol),
                    testTag = "target_amount_input"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mostrar Tasa Anual si no es REQUIRED_RATE
                if (uiState.calculationType != CalculationType.REQUIRED_RATE) {
                    PercentageInputField(
                        value = uiState.annualRate,
                        onValueChange = onAnnualRateChange,
                        label = stringResource(R.string.label_annual_rate),
                        testTag = "annual_rate_input",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Mostrar Años si no es TIME_TO_GOAL
                if (uiState.calculationType != CalculationType.TIME_TO_GOAL) {
                    YearsInputField(
                        value = uiState.years,
                        onValueChange = onYearsChange,
                        label = stringResource(R.string.label_years),
                        testTag = "years_input",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            FrequencyDropdown(
                selectedFrequency = uiState.compoundingFrequency,
                onFrequencyChange = onFrequencyChange
            )

            // Mostrar Aportación Mensual si no es REQUIRED_CONTRIBUTION
            if (uiState.calculationType != CalculationType.REQUIRED_CONTRIBUTION) {
                CurrencyInputField(
                    value = uiState.periodicContribution,
                    onValueChange = onPeriodicContributionChange,
                    label = stringResource(R.string.label_periodic_contribution, uiState.selectedCurrency.curSymbol),
                    testTag = "monthly_contribution_input"
                )
            }

            TimingSelector(
                selectedTiming = uiState.contributionTiming,
                onTimingChange = onTimingChange
            )

            Button(
                onClick = onCalculate,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF3F51B5), Color(0xFFE91E63))
                            ),
                            shape = MaterialTheme.shapes.large
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.btn_calculate),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.2.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ResultsSection(
    result: InterestCalculationResult,
    calculationType: CalculationType,
    years: String,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        
        // Tarjeta de Resultado Principal Dinámica
        when (calculationType) {
            CalculationType.FUTURE_VALUE -> {
                ResultValueCard(
                    title = stringResource(R.string.result_final_value_title),
                    value = result.finalValue,
                    subtitle = stringResource(R.string.result_final_value_subtitle, years.toIntOrNull() ?: 0),
                    icon = Icons.Rounded.Diamond,
                    accentColor = Color(0xFF3F51B5),
                    currencySymbol = currencySymbol
                )
            }
            CalculationType.TIME_TO_GOAL -> {
                val totalYears = result.yearlyBreakdown.size
                ResultValueCard(
                    title = stringResource(R.string.label_years),
                    value = totalYears.toDouble(),
                    subtitle = stringResource(R.string.type_time_to_goal),
                    icon = Icons.Rounded.Timer,
                    accentColor = Color(0xFF3F51B5),
                    isCurrency = false,
                )
            }
            CalculationType.REQUIRED_CONTRIBUTION -> {
                ResultValueCard(
                    title = stringResource(R.string.label_periodic_contribution),
                    value = (result.totalContributions / (result.yearlyBreakdown.size * 12)),
                    subtitle = stringResource(R.string.type_required_contribution),
                    icon = Icons.Rounded.Savings,
                    accentColor = Color(0xFF3F51B5),
                    currencySymbol = currencySymbol
                )
            }
            CalculationType.REQUIRED_RATE -> {
                ResultValueCard(
                    title = stringResource(R.string.label_annual_rate),
                    value = result.totalInterest, // Reutilizado como tasa en el UseCase
                    subtitle = stringResource(R.string.type_required_rate),
                    icon = Icons.Rounded.Percent,
                    accentColor = Color(0xFF3F51B5),
                    isCurrency = false,
                    suffix = "%",
                )
            }
        }

        ResultValueCard(
            title = stringResource(R.string.result_interest_title),
            value = if (calculationType == CalculationType.REQUIRED_RATE) result.finalValue - result.initialCapital - result.totalContributions else result.totalInterest,
            subtitle = stringResource(R.string.result_interest_subtitle),
            icon = Icons.Rounded.BarChart,
            accentColor = Color(0xFFE91E63),
            currencySymbol = currencySymbol
        )

        DetailedBreakdownCard(
            initialCapital = result.initialCapital,
            totalContributions = result.totalContributions,
            totalInterest = if (calculationType == CalculationType.REQUIRED_RATE) result.finalValue - result.initialCapital - result.totalContributions else result.totalInterest,
            finalValue = result.finalValue,
            currencySymbol = currencySymbol
        )

        InterestChart(yearlyBreakdown = result.yearlyBreakdown.toImmutableList())
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun MainScreenPreview() {
    CompoundInterestMasterTheme {
        MainScreen()
    }
}
