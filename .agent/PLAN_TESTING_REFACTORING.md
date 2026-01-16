# Plan de Actualización: Testing y Refactorización (Journeys)

Este documento detalla el plan para fortalecer la calidad del software mediante una estrategia de testing integral y la implementación de "Journeys" para validar flujos de usuario.

## 1. Estrategia de Testing

### A. Pruebas Unitarias (Local Tests - `test`)
*   **Domain Layer:**
    *   `CalculateCompoundInterestUseCaseTest`: Validar la precisión de los cálculos matemáticos con diferentes frecuencias de capitalización, aportaciones mensuales y tasas.
*   **Presentation Layer:**
    *   `MainViewModelTest`: Probar la lógica del ViewModel, el manejo del estado (`UiState`) y la reactividad ante cambios en los campos de entrada.

### B. Pruebas de Instrumentación (Connected Tests - `androidTest`)
*   **UI Components:**
    *   `FinancialInputFieldsTest`: Validar que los campos de texto formateen correctamente la moneda y los porcentajes.
    *   `FrequencyDropdownTest`: Asegurar que el selector de frecuencia actualice el estado correctamente.
*   **Screen Level:**
    *   `MainScreenTest`: Verificar que los resultados se muestren/oculten correctamente al presionar el botón de calcular.

### C. End-to-End (E2E) y Journeys
*   Implementación de **User Journeys** en `androidTest/java/com/.../journeys/`.
*   **Journey: "Cálculo Exitoso de Ahorro a Largo Plazo"**
    *   Flujo: Abrir app -> Ingresar capital inicial -> Configurar tasa y años -> Añadir aportación mensual -> Calcular -> Verificar resultados finales.

## 2. Implementación de Journeys
Utilizaremos el patrón de **Testing Journeys** para simular recorridos reales de usuario, organizando los tests por objetivos de negocio en lugar de por componentes técnicos.

*   **Carpeta:** `app/src/androidTest/java/com/feryaeljustice/compoundinterestmaster/journeys/`
*   **Foco:** Validar que la experiencia del usuario sea fluida y los cálculos finales en pantalla coincidan con las expectativas del negocio.

## 3. Guía de Ejecución
1.  **Unit Tests:** `./gradlew test`
2.  **Instrumentation Tests:** `./gradlew connectedAndroidTest`

---
**Nota Importante:** Este plan respeta la restricción de NO modificar archivos de configuración de Gradle (`build.gradle.kts`) ni de versiones (`libs.versions.toml`). Se utilizarán las dependencias ya existentes en el proyecto (JUnit4, Compose UI Test, Hilt Testing).
