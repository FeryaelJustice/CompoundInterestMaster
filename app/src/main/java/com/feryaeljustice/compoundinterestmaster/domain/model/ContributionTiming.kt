/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.model

import androidx.annotation.StringRes
import com.feryaeljustice.compoundinterestmaster.R

enum class ContributionTiming(@param:StringRes val displayResId: Int) {
    BEGINNING(R.string.timing_start),
    END(R.string.timing_end)
}
