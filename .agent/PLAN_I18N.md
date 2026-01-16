# Plan de Internacionalización (i18n) - Multiidioma

Este documento detalla el plan para habilitar soporte multiidioma en la aplicación, estableciendo el Español como idioma predeterminado e integrando el Inglés.

## 1. Objetivos
- Migrar todos los textos hardcoded a recursos de strings (`strings.xml`).
- Mantener el nombre de la aplicación `app_name` como no traducible.
- Configurar el soporte para Español (default) e Inglés (`en`).

## 2. Inventario de Textos a Localizar

### Pantalla Principal (MainScreen)
- `💰 Calculadora de Interés Compuesto` -> `calc_title`
- `Descubre cómo crece tu dinero con el tiempo` -> `calc_subtitle`
- `CALCULAR` -> `btn_calculate`
- `RESULTADOS PROYECTADOS` -> `results_header`
- `DETALLE POR AÑO` -> `breakdown_header`

### Campos de Entrada (FinancialInputFields)
- `Capital Inicial (€)` -> `label_initial_capital`
- `Tasa Anual (%)` -> `label_annual_rate`
- `Años` -> `label_years`
- `Aportación Mensual (€)` -> `label_monthly_contribution`
- `Frecuencia de Capitalización` -> `label_compounding_frequency`

### Componentes de Resultados (ResultComponents)
- `Valor Final` -> `result_final_value_title`
- `Intereses Ganados` -> `result_interest_title`
- `Total después de %d años` -> `result_final_value_subtitle`
- `Ganancias por interés compuesto` -> `result_interest_subtitle`
- `Desglose` -> `breakdown_title`
- `Capital inicial:` -> `row_initial_capital`
- `Aportaciones:` -> `row_contributions`
- `Intereses:` -> `row_interest`
- `TOTAL:` -> `row_total`
- `AÑO` -> `table_header_year`
- `BALANCE` -> `table_header_balance`

### Frecuencias (Enums)
- `Diaria` -> `freq_daily`
- `Mensual` -> `freq_monthly`
- `Trimestral` -> `freq_quarterly`
- `Semestral` -> `freq_semiannually`
- `Anual` -> `freq_annually`

## 3. Pasos de Implementación
1.  **Creación de Recursos**:
    - Actualizar `values/strings.xml` con los textos en Español.
    - Crear `values-en/strings.xml` con las traducciones al Inglés.
2.  **Refactorización de UI**:
    - Sustituir strings literales por `stringResource(id = ...)` en todos los Composables.
3.  **Refactorización de Enums**:
    - Añadir una propiedad `displayResId` al enum `CompoundingFrequency` para manejar traducciones de las opciones del dropdown.
4.  **Validación**:
    - Verificar que los tests unitarios y de instrumentación sigan pasando (ajustar si buscaban textos literales).

---
**Nota**: El nombre `Compound Interest Master` se mantendrá igual en ambos idiomas mediante `translatable="false"` en el recurso base.
