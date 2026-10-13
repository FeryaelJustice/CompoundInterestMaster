/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.feryaeljustice.compoundinterestmaster.domain.manager.AdManager
import com.feryaeljustice.compoundinterestmaster.presentation.MainScreen
import com.feryaeljustice.compoundinterestmaster.ui.theme.CompoundInterestMasterTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        adManager.initialize()

        setContent {
            CompoundInterestMasterTheme {
                MainScreen(
                    onTriggerInterstitial = {
                        adManager.showInterstitial(this@MainActivity)
                    }
                )
            }
        }
    }
}