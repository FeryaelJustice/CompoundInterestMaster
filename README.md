# Compound Interest Master 📈

**Compound Interest Master** es una aplicación de calculadora financiera de alto rendimiento diseñada con las últimas prácticas de desarrollo moderno en Android. Permite a los usuarios proyectar sus ahorros a largo plazo utilizando el poder del interés compuesto con una interfaz vibrante y expresiva.

## ✨ Características Principales

- **Motor de Interés Compuesto**: Lógica financiera de alta precisión que soporta capital inicial, tasas de interés, diferentes frecuencias de capitalización y aportaciones mensuales.
- **Interfaz Material 3 Expressive**: Diseño moderno y vibrante con soporte completo para Edge-to-Edge, utilizando una paleta de colores energética (Azul, Tecla y Oro).
- **Suite de Pruebas Integral**: 
    - **Unit Tests**: Validación exhaustiva de la lógica de negocio y ViewModels.
    - **Instrumentation Tests**: Pruebas de componentes de UI con Compose.
    - **User Journeys (E2E)**: Validación de flujos de usuario completos utilizando el patrón **Robot**.
- **Arquitectura Robusta**: Implementación limpia siguiendo el patrón MVVM y Clean Architecture (Usecases).
- **Sistema de Build Optimizado**: Uso de Version Catalogs (toml) y KSP para tiempos de compilación más rápidos.

## 🛠️ Stack Tecnológico

- **Lenguaje**: Kotlin (2.3.0)
- **UI**: Jetpack Compose con Material 3
- **Inyección de Dependencias**: Hilt con KSP
- **Arquitectura**: MVVM + StateFlow + Usecases
- **Testing**: 
    - JUnit 4
    - Compose UI Test
    - Hilt Testing (con Custom Test Runner)
    - Coroutines Test Dispatchers

## 🧪 Estrategia de Testing

La aplicación sigue una pirámide de pruebas equilibrada:
1. **Unitarias**: Localizadas en `app/src/test`. Cubren el `CalculateCompoundInterestUseCase` y el `MainViewModel`.
2. **Instrumentación**: Localizadas en `app/src/androidTest`. Verifican el comportamiento de los componentes de UI.

Para ejecutar las pruebas:
```bash
# Pruebas Unitarias
./gradlew test

# Pruebas de Instrumentación
./gradlew connectedAndroidTest
```

## 🚀 Instalación y Build

1. Clonar el repositorio.
2. Abrir en **Android Studio Ladybug** o superior.
3. Sincronizar Gradle.
4. Ejecutar la tarea `:app:assembleDebug`.

---
**Desarrollado con un enfoque en la calidad de código y la experiencia de usuario.**
