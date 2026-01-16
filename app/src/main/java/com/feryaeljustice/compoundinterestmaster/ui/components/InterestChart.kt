/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feryaeljustice.compoundinterestmaster.domain.model.YearlyGrowth
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun InterestChart(
    yearlyBreakdown: List<YearlyGrowth>,
    modifier: Modifier = Modifier
) {
    if (yearlyBreakdown.isEmpty()) return

    // Cambia estos 2 campos si tu YearlyGrowth usa otros nombres.
    val xValues = remember(yearlyBreakdown) { yearlyBreakdown.map { it.year } }
    val yValues = remember(yearlyBreakdown) { yearlyBreakdown.map { it.totalInterest } }

    val modelProducer = remember { CartesianChartModelProducer() }

    // Carga dinámica real: cada vez que cambian los datos, se actualiza el modelo del chart.
    LaunchedEffect(xValues, yValues) {
        modelProducer.runTransaction {
            lineSeries {
                series(x = xValues, y = yValues)
            }
        }
    }

    val lineColor = Color(0xFFE91E63)
    val areaTop = lineColor.copy(alpha = 0.30f)
    val areaBottom = Color.Transparent

    val line = LineCartesianLayer.rememberLine(
        fill = LineCartesianLayer.LineFill.single(Fill(lineColor)),
        areaFill = LineCartesianLayer.AreaFill.single(
            fill = Fill(
                Brush.verticalGradient(
                    colors = listOf(areaTop, areaBottom)
                )
            )
        ),
        pointProvider = LineCartesianLayer.PointProvider.single(
            point = LineCartesianLayer.Point(
                component = rememberShapeComponent(
                    fill = Fill(lineColor),
                    shape = RectangleShape
                ),
                size = 8.dp
            )
        )
    )

    val layer = rememberLineCartesianLayer(
        lineProvider = LineCartesianLayer.LineProvider.series(line)
    )

    val yFormatter = remember {
        CartesianValueFormatter { _, value, _ -> formatEuroCompact(value) }
    }

    val xFormatter = remember {
        CartesianValueFormatter { _, value, _ ->
            val year = value.roundToInt()
            year.toString()
        }
    }

    val markerLabel = rememberTextComponent(
        style = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        ),
        padding = Insets(horizontal = 8.dp, vertical = 4.dp),
        background = rememberShapeComponent(
            fill = Fill(MaterialTheme.colorScheme.surface),
            shape = RectangleShape,
            strokeFill = Fill(MaterialTheme.colorScheme.outlineVariant),
            strokeThickness = 1.dp
        )
    )

    val marker = rememberDefaultCartesianMarker(label = markerLabel)

    val chart = rememberCartesianChart(
        layer,
        startAxis = VerticalAxis.rememberStart(valueFormatter = yFormatter),
        bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = xFormatter),
        marker = marker
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ShowChart,
                    contentDescription = null,
                    tint = lineColor
                )
                Spacer(modifier = Modifier.padding(6.dp))
                Text(
                    text = "Evolución anual",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            // “Panel” sutil para que el chart quede integrado y no flotando raro
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(10.dp)
            ) {
                CartesianChartHost(
                    chart = chart,
                    modelProducer = modelProducer,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = "Años (X) y capital final estimado (Y)",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

private fun formatEuroCompact(value: Double): String {
    val absValue = abs(value)
    return when {
        absValue >= 1_000_000 -> "€" + trimZeros(value / 1_000_000) + "M"
        absValue >= 1_000 -> "€" + trimZeros(value / 1_000) + "k"
        else -> "€" + trimZeros(value)
    }
}

private fun trimZeros(v: Double): String {
    val s = String.format(java.util.Locale.US, "%.2f", v)
    return s.trimEnd('0').trimEnd('.')
}
