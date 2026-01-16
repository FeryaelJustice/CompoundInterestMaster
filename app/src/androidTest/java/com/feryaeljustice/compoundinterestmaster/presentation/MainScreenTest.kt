/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.feryaeljustice.compoundinterestmaster.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testInputFieldsAndCalculationTrigger() {
        // Initial Capital field should exist with default value "10000"
        composeTestRule.onNodeWithTag("initial_capital_input").assertExists()
        composeTestRule.onNodeWithText("10000").assertExists()

        // Annual Rate field should exist
        composeTestRule.onNodeWithTag("annual_rate_input").assertExists()

        // Years field should exist
        composeTestRule.onNodeWithTag("years_input").assertExists()

        // Monthly Contribution field should exist with default value "1200"
        composeTestRule.onNodeWithTag("monthly_contribution_input").assertExists()
        composeTestRule.onNodeWithText("1200").assertExists()

        // Button should exist
        composeTestRule.onNodeWithText("CALCULAR").assertExists()

        // Click button
        composeTestRule.onNodeWithText("CALCULAR").performClick()

        // Results should appear
        composeTestRule.onNodeWithText("Valor Final").assertExists()
        composeTestRule.onNodeWithText("Intereses Ganados").assertExists()
        composeTestRule.onNodeWithText("Desglose").assertExists()
    }

    @Test
    fun testChangeInputsAndRecalculate() {
        // Clear and type new value for Initial Capital
        composeTestRule.onNodeWithText("10000").performTextClearance()
        composeTestRule.onNodeWithTag("initial_capital_input").performTextInput("5000")

        // Click calculate
        composeTestRule.onNodeWithText("CALCULAR").performClick()

        // Check if results reflect the change (approximate check of existence)
        composeTestRule.onNodeWithText("Valor Final").assertIsDisplayed()
    }
}