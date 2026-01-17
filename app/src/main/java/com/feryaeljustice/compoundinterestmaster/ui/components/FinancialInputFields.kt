/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Money
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.feryaeljustice.compoundinterestmaster.R
import com.feryaeljustice.compoundinterestmaster.domain.model.CICurrency
import com.feryaeljustice.compoundinterestmaster.domain.model.CalculationType
import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming

@Composable
fun CurrencyInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    testTag: String = "",
) {
    ExpressiveTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = Icons.Rounded.Money, // Idealmente cambiaríamos el icono según la moneda si fuera dinámico
        modifier = modifier.testTag(testTag)
    )
}

@Composable
fun PercentageInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    ExpressiveTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = Icons.Rounded.Percent,
        modifier = modifier.testTag(testTag)
    )
}

@Composable
fun YearsInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    ExpressiveTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = Icons.Rounded.Schedule,
        modifier = modifier.testTag(testTag)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationTypeSelector(
    selectedType: CalculationType,
    onTypeChange: (CalculationType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val types = CalculationType.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = stringResource(id = selectedType.displayResId),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.label_calculation_type)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
                .testTag("calculation_type_dropdown"),
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(stringResource(id = type.displayResId)) },
                    onClick = {
                        onTypeChange(type)
                        expanded = false
                    },
                    modifier = Modifier.testTag("type_item_${type.name}")
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyDropdown(
    selectedFrequency: CompoundingFrequency,
    onFrequencyChange: (CompoundingFrequency) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val frequencies = CompoundingFrequency.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = stringResource(id = selectedFrequency.displayResId),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.label_compounding_frequency)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
                .testTag("frequency_dropdown"),
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            frequencies.forEach { frequency ->
                DropdownMenuItem(
                    text = { Text(stringResource(id = frequency.displayResId)) },
                    onClick = {
                        onFrequencyChange(frequency)
                        expanded = false
                    },
                    modifier = Modifier.testTag("frequency_item_${frequency.name}")
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimingSelector(
    selectedTiming: ContributionTiming,
    onTimingChange: (ContributionTiming) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val timings = ContributionTiming.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = stringResource(id = selectedTiming.displayResId),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.label_contribution_timing)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
                .testTag("timing_dropdown"),
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            timings.forEach { timing ->
                DropdownMenuItem(
                    text = { Text(stringResource(id = timing.displayResId)) },
                    onClick = {
                        onTimingChange(timing)
                        expanded = false
                    },
                    modifier = Modifier.testTag("timing_item_${timing.name}")
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelector(
    selectedCurrency: CICurrency,
    currencies: List<CICurrency>,
    onCurrencyChange: (CICurrency) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = "${selectedCurrency.curName} (${selectedCurrency.curSymbol})",
            onValueChange = {},
            readOnly = true,
            label = { Text("Moneda") }, // Idealmente a string resource
            leadingIcon = { Icon(Icons.Rounded.Language, contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
                .testTag("currency_dropdown"),
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text("${currency.curName} (${currency.curSymbol})") },
                    onClick = {
                        onCurrencyChange(currency)
                        expanded = false
                    },
                    modifier = Modifier.testTag("currency_item_${currency.curSymbol}")
                )
            }
        }
    }
}

@Composable
fun ExpressiveTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        singleLine = true
    )
}
