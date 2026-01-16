/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.domain.usecase

import com.feryaeljustice.compoundinterestmaster.domain.model.CompoundingFrequency
import com.feryaeljustice.compoundinterestmaster.domain.model.ContributionTiming
import com.feryaeljustice.compoundinterestmaster.domain.model.InterestCalculationResult
import javax.inject.Inject

class CalculateRequiredRateUseCase @Inject constructor(
    private val calculateFutureValueUseCase: CalculateFutureValueUseCase
) {

    operator fun invoke(
        initialCapital: Double,
        targetAmount: Double,
        years: Int,
        compoundingFrequency: CompoundingFrequency,
        periodicContribution: Double,
        contributionTiming: ContributionTiming
    ): InterestCalculationResult {
        // Encontrar la tasa necesaria es un problema de búsqueda de raíces (ej. método de bisección)
        // ya que no hay una fórmula cerrada sencilla para 'r' con aportaciones periódicas.
        
        var lowRate = 0.0
        var highRate = 100.0 // 100% como límite inicial razonable
        var midRate = 0.0
        
        // Iteramos para encontrar la tasa que se acerque al objetivo
        for (i in 0..20) { // 20 iteraciones dan buena precisión
            midRate = (lowRate + highRate) / 2.0
            val result = calculateFutureValueUseCase(
                initialCapital = initialCapital,
                annualRate = midRate,
                years = years,
                compoundingFrequency = compoundingFrequency,
                periodicContribution = periodicContribution,
                contributionTiming = contributionTiming
            )
            
            if (result.finalValue < targetAmount) {
                lowRate = midRate
            } else {
                highRate = midRate
            }
        }

        // Devolvemos el resultado final con la tasa encontrada
        return calculateFutureValueUseCase(
            initialCapital = initialCapital,
            annualRate = midRate,
            years = years,
            compoundingFrequency = compoundingFrequency,
            periodicContribution = periodicContribution,
            contributionTiming = contributionTiming
        ).copy(totalInterest = midRate) // Reutilizamos totalInterest temporalmente para pasar la tasa encontrada si fuera necesario, 
                                        // pero lo ideal es que el UI maneje el resultado de la tasa.
                                        // En este caso, el result.finalValue ya será cercano al targetAmount.
    }
}
