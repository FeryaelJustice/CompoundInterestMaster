# AGENTS.md - Registro de Colaboración de IA

Este documento detalla la intervención de agentes de IA en el ciclo de vida del proyecto **Compound Interest Master**, registrando sus roles, contribuciones y el historial detallado de cambios realizados para asegurar la transparencia y trazabilidad del desarrollo.

## 📱 Descripción de la Aplicación
**Compound Interest Master** es una calculadora financiera avanzada construida con **Jetpack Compose** y **Material 3 Expressive**. Su objetivo es proporcionar proyecciones precisas de crecimiento patrimonial mediante el interés compuesto, ofreciendo una experiencia de usuario vibrante, animada y altamente intuitiva. Soporta configuraciones de capital inicial, aportaciones mensuales, tasas variables y diversas frecuencias de capitalización.

## 🤖 Agentes y Roles

### 1. Architect Agent
*   **Misión**: Definir la estructura robusta y escalable.
*   **Aportes**: Diseño de arquitectura MVVM + Clean Architecture, configuración de Hilt para DI y adopción de Version Catalogs con KSP.

### 2. Feature Developer Agent
*   **Misión**: Transformar requerimientos en funcionalidad y UI.
*   **Aportes**: Implementación del motor matemático de interés, creación de componentes de UI expresivos y gestión de estados con StateFlow.

### 3. QA & SDET Agent
*   **Misión**: Garantizar la calidad y estabilidad del software.
*   **Aportes**: Creación de la suite de pruebas integral (Unit, Instrumentation) y optimización del sistema de build.

## 📈 Historial de Cambios (Change Log)

Basado en la ejecución de los planes estratégicos del proyecto:

### Fase 1: Cimientos y Dominio (Task 1)
- **Hilt Initialization**: Configuración de la clase Application y puntos de entrada.
- **Domain Logic**: Implementación de `CalculateCompoundInterestUseCase` con soporte para múltiples parámetros financieros.
- **Edge-to-Edge**: Habilitación de visualización inmersiva en `MainActivity`.

### Fase 2: Identidad Visual y UI (Task 2)
- **Material 3 Expressive**: Implementación de un esquema de colores vibrantes (Azul Energético, Teal y Oro).
- **Custom Components**: Creación de campos de entrada financieros y tarjetas de resultados con formateo localizado.

### Fase 3: Integración MVVM (Task 3)
- **ViewModel & State**: Gestión de la reactividad de la UI y lógica de cálculo en tiempo real.
- **Main Screen**: Construcción de la pantalla principal con gradientes y formularios dinámicos.

### Fase 4: Pulido y Recursos (Task 4)
- **Adaptive Icon**: Creación de un icono moderno y adaptable.
- **Animations**: Implementación de animaciones basadas en resortes (Spring) para la sección de resultados.

### Fase 5: Verificación y Estabilidad (Task 5)
- **Accuracy Check**: Validación manual de cálculos comparados con fórmulas financieras estándar.
- **Stability**: Pruebas de rendimiento y ausencia de crashes.

### Fase 6: Optimización de Build (Task 6)
- **Gradle Refactor**: Limpieza de `build.gradle.kts`, simplificación de sintaxis SDK y migración completa a KSP.
- **Cleanup**: Eliminación de configuraciones obsoletas de demonios de Gradle.

### Fase 7: Resiliencia y Testing (Plan de Refactorización)
- **Unit Testing**: Suite para validar el Use Case y el ViewModel.
- **Instrumentation**: Pruebas de componentes de Compose.
- **Hilt Test Runner**: Configuración de entorno de pruebas con inyección de dependencias.

### Fase 8: Integración de Monetización con Google AdMob
- **AdMob SDK 25.5.0**: Adopción de la versión más reciente y estable en Version Catalogs (`libs.versions.toml`).
- **Clean Architecture & Hilt**: Definición de la interfaz `AdManager` en capa de dominio e implementación de `AdMobManager` en `data/manager`, desacoplados mediante inyección con Hilt (`AdModule`).
- **Gestión Segura de Identificadores**: Configuración en `app/build.gradle.kts` y `local.properties` con placeholders de manifiesto para `APPLICATION_ID` y campo de compilación `ADMOB_INTERSTITIAL_AD_UNIT_ID`. Configuración por defecto de IDs oficiales de prueba de Google para prevenir tráfico no válido y suspensión de cuenta.
- **Experiencia de Usuario y Políticas de Play Store**: Regulación de frecuencia y periodo de enfriamiento (cooldown) para anuncios intersticiales, respetando el ciclo de vida y Better Ads Standards.
- **Incremento de Versión**: Subida a `versionCode = 5` y `versionName = "1.2.0"`.
- **Verificación Completa**: Ejecución exitosa de pruebas unitarias y generación validada de `assembleDebug` y `assembleRelease` con R8.

### Fase 9: Integración de Firebase y Google Analytics
- **Version Catalog & Plugins**: Adopción de `googleServices = "4.4.2"` y `firebaseBom = "35.0.0"`. Aplicación del plugin `com.google.gms.google-services` tanto en el raíz como en el módulo `app`.
- **Google Services JSON**: Ubicación y vinculación correcta de `app/google-services.json` para el paquete `com.feryaeljustice.compoundinterestmaster`.
- **Inicialización y Arquitectura Limpia**:
  - Inicialización explícita y segura de `FirebaseApp` en `CompoundInterestApplication`.
  - Definición de la interfaz `AnalyticsManager` en la capa de dominio.
  - Implementación de `FirebaseAnalyticsManager` en `data/manager`, inyectada mediante Hilt con `AnalyticsModule`.
- **Instrumentación de Eventos en la App**:
  - Registro de evento `calculation_performed` con capital inicial, tasa, años y divisa en cada cálculo.
  - Registro de eventos de interacción de usuario: `calculation_type_selected` y `currency_selected`.
  - Registro de impresión de anuncios: `ad_impression_custom` sincronizado con la presentación de intersticiales en `AdMobManager`.
- **Verificación Completa**: Procesamiento exitoso de recursos Google Services (`processDebugGoogleServices`, `processReleaseGoogleServices`), pruebas unitarias superadas al 100% y builds `assembleDebug` y `assembleRelease` generados con éxito.


## 🛠️ Stack de IA y Herramientas
- **Android Studio Ladybug (Gemini/Agentic Features)**.
- **KSP (Kotlin Symbol Processing)** para optimización de agentes de generación de código.
- **Google Mobile Ads SDK 25.5.0**.
- **Firebase BoM 35.0.0** y **Firebase Analytics**.
- **Google Services Gradle Plugin 4.4.2**.

---
*Este registro es actualizado dinámicamente para reflejar la evolución del sistema.*


