package com.feryaeljustice.compoundinterestmaster.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Euro
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
import androidx.compose.ui.tooling.preview.Preview
import com.feryaeljustice.compoundinterestmaster.R
import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.ui.theme.CompoundInterestMasterTheme

@Composable
fun CurrencyInputField(
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
        leadingIcon = Icons.Rounded.Euro,
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
fun FrequencyDropdown(
    selectedFrequency: CompoundingFrequency,
    onFrequencyChange: (CompoundingFrequency) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val frequencies = CompoundingFrequency.entries.toTypedArray()

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

@Preview(showBackground = true)
@Composable
private fun InputFieldsPreview() {
    CompoundInterestMasterTheme {
        CurrencyInputField(value = "1000", onValueChange = {}, label = "Initial Investment")
    }
}
