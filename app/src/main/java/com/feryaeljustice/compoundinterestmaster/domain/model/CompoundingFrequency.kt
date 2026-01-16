/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.model

import androidx.annotation.StringRes
import com.feryaeljustice.compoundinterestmaster.R

enum class CompoundingFrequency(val timesPerYear: Int, @param:StringRes val displayResId: Int) {
    ANNUALLY(1, R.string.freq_annually),
    MONTHLY(12, R.string.freq_monthly),
    BIWEEKLY(26, R.string.freq_biweekly),
    WEEKLY(52, R.string.freq_weekly)
}
