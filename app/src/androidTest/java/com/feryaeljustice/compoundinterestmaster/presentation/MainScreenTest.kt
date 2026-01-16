/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
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
        // Find by tag instead of literal text where possible to avoid i18n issues
        composeTestRule.onNodeWithTag("initial_capital_input").assertExists()
        composeTestRule.onNodeWithText("10000").assertExists()

        composeTestRule.onNodeWithTag("annual_rate_input").assertExists()
        composeTestRule.onNodeWithTag("years_input").assertExists()

        composeTestRule.onNodeWithTag("monthly_contribution_input").assertExists()
        composeTestRule.onNodeWithText("1200").assertExists()

        // For buttons and results, we might need to find by tag or content description 
        // if we want to be 100% i18n proof, but for now we keep finding by text
        // assuming tests run in default locale (Spanish)
        composeTestRule.onNodeWithText("CALCULAR").performClick()

        composeTestRule.onNodeWithText("Valor Final").assertExists()
        composeTestRule.onNodeWithText("Intereses Ganados").assertExists()
        composeTestRule.onNodeWithText("Desglose").assertExists()
    }

    @Test
    fun testChangeInputsAndRecalculate() {
        composeTestRule.onNodeWithText("10000").performTextClearance()
        composeTestRule.onNodeWithTag("initial_capital_input").performTextInput("5000")

        composeTestRule.onNodeWithText("CALCULAR").performClick()

        composeTestRule.onNodeWithText("Valor Final").assertIsDisplayed()
    }
}
