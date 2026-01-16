/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.feryaeljustice.compoundinterestmaster.ui.components.*
import com.feryaeljustice.compoundinterestmaster.ui.theme.CompoundInterestMasterTheme

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                text = "💰 Calculadora de Interés Compuesto",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Descubre cómo crece tu dinero con el tiempo",
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
    onCalculate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                label = "Capital Inicial (€)",
                testTag = "initial_capital_input"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PercentageInputField(
                    value = uiState.annualRate,
                    onValueChange = onAnnualRateChange,
                    label = "Tasa Anual (%)",
                    testTag = "annual_rate_input",
                    modifier = Modifier.weight(1f)
                )
                YearsInputField(
                    value = uiState.years,
                    onValueChange = onYearsChange,
                    label = "Años",
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
                label = "Aportación Mensual (€)",
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
                        text = "CALCULAR",
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
    years: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ResultValueCard(
            title = "Valor Final",
            value = finalValue,
            subtitle = "Total después de $years años",
            icon = Icons.Rounded.Diamond,
            accentColor = Color(0xFF3F51B5)
        )
        
        ResultValueCard(
            title = "Intereses Ganados",
            value = totalInterest,
            subtitle = "Ganancias por interés compuesto",
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
fun MainScreenPreview() {
    CompoundInterestMasterTheme {
        MainScreen()
    }
}