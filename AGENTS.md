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

## 🛠️ Stack de IA y Herramientas
- **Android Studio Ladybug (Gemini/Agentic Features)**.
- **KSP (Kotlin Symbol Processing)** para optimización de agentes de generación de código.

---
*Este registro es actualizado dinámicamente para reflejar la evolución del sistema.*
