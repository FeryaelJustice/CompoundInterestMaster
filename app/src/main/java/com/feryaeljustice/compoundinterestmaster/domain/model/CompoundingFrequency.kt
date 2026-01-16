/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.model

import androidx.annotation.StringRes
import com.feryaeljustice.compoundinterestmaster.R

enum class CompoundingFrequency(val timesPerYear: Int, @param:StringRes val displayResId: Int) {
    ANNUALLY(1, R.string.freq_annually),
    SEMI_ANNUALLY(2, R.string.freq_semiannually),
    QUARTERLY(4, R.string.freq_quarterly),
    MONTHLY(12, R.string.freq_monthly),
    DAILY(365, R.string.freq_daily)
}