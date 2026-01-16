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
import com.feryaeljustice.compoundinterestmaster.ui.components.CurrencyInputField
import com.feryaeljustice.compoundinterestmaster.ui.components.DetailedBreakdownCard
import com.feryaeljustice.compoundinterestmaster.ui.components.FrequencyDropdown
import com.feryaeljustice.compoundinterestmaster.ui.components.PercentageInputField
import com.feryaeljustice.compoundinterestmaster.ui.components.ResultValueCard
import com.feryaeljustice.compoundinterestmaster.ui.components.YearsInputField
import com.feryaeljustice.compoundinterestmaster.ui.theme.CompoundInterestMasterTheme

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
                    onInitialCapitalChange = viewModel::onInitialCapitalChange,
                    onAnnualRateChange = viewModel::onAnnualRateChange,
                    onYearsChange = viewModel::onYearsChange,
                    onFrequencyChange = viewModel::onCompoundingFrequencyChange,
                    onMonthlyContributionChange = viewModel::onMonthlyContributionChange,
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
                    uiState.result?.let { result ->
                        ResultsSection(
                            finalValue = result.finalValue,
                            totalInterest = result.totalInterest,
                            initialCapital = result.initialCapital,
                            totalContributions = result.totalContributions,
                            years = uiState.years
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
    onInitialCapitalChange: (String) -> Unit,
    onAnnualRateChange: (String) -> Unit,
    onYearsChange: (String) -> Unit,
    onFrequencyChange: (com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency) -> Unit,
    onMonthlyContributionChange: (String) -> Unit,
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
            CurrencyInputField(
                value = uiState.initialCapital,
                onValueChange = onInitialCapitalChange,
                label = stringResource(R.string.label_initial_capital),
                testTag = "initial_capital_input"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PercentageInputField(
                    value = uiState.annualRate,
                    onValueChange = onAnnualRateChange,
                    label = stringResource(R.string.label_annual_rate),
                    testTag = "annual_rate_input",
                    modifier = Modifier.weight(1f)
                )
                YearsInputField(
                    value = uiState.years,
                    onValueChange = onYearsChange,
                    label = stringResource(R.string.label_years),
                    testTag = "years_input",
                    modifier = Modifier.weight(1f)
                )
            }

            FrequencyDropdown(
                selectedFrequency = uiState.compoundingFrequency,
                onFrequencyChange = onFrequencyChange
            )

            CurrencyInputField(
                value = uiState.monthlyContribution,
                onValueChange = onMonthlyContributionChange,
                label = stringResource(R.string.label_monthly_contribution),
                testTag = "monthly_contribution_input"
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
    finalValue: Double,
    totalInterest: Double,
    initialCapital: Double,
    totalContributions: Double,
    years: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ResultValueCard(
            title = stringResource(R.string.result_final_value_title),
            value = finalValue,
            subtitle = stringResource(R.string.result_final_value_subtitle, years),
            icon = Icons.Rounded.Diamond,
            accentColor = Color(0xFF3F51B5)
        )
        
        ResultValueCard(
            title = stringResource(R.string.result_interest_title),
            value = totalInterest,
            subtitle = stringResource(R.string.result_interest_subtitle),
            icon = Icons.Rounded.BarChart,
            accentColor = Color(0xFFE91E63)
        )
        
        DetailedBreakdownCard(
            initialCapital = initialCapital,
            totalContributions = totalContributions,
            totalInterest = totalInterest,
            finalValue = finalValue
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun MainScreenPreview() {
    CompoundInterestMasterTheme {
        MainScreen()
    }
}
