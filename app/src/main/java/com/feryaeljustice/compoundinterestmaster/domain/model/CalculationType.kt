/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.model

import androidx.annotation.StringRes
import com.feryaeljustice.compoundinterestmaster.R

enum class CalculationType(@param:StringRes val displayResId: Int) {
    FUTURE_VALUE(R.string.type_future_value),
    TIME_TO_GOAL(R.string.type_time_to_goal),
    REQUIRED_CONTRIBUTION(R.string.type_required_contribution),
    REQUIRED_RATE(R.string.type_required_rate)
}
