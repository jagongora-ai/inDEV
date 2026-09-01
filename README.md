# inDEV
Bienvenido a **inDEV**. Este repositorio contiene las directrices, estándares y flujo de trabajo para el desarrollo del proyecto

# Temas y Buenas Prácticas de Desarrollo
- **Control de versiones:** Uso de Git y GitHub para el trabajo colaborativo.
- **Flujo de trabajo:** Implementación estricta del modelo **GitFlow**.
- **Buenas prácticas:** Historial de commits limpio, ordenado y descriptivo.
- **Automatización & CI/CD:** Configuración de workflows con GitHub Actions (ejecución de pruebas, construcción de artefactos y análisis de calidad de código con SonarQube).
- **Documentación estándar:** Mantenimiento de archivos clave como `README.md`, `CONTRIBUTING.md`, `LICENSE.md` y `CHANGELOG.md`.

# Flujo de Trabajo: GitFlow
En este proyecto utilizamos la metodología **GitFlow** para el manejo de ramas y lanzamientos.

### 🌿 Ramas Principales

1. **`main`**: Código estable y desplegado en producción. Cada unión en `main` debe ir acompañada de un número de versión (*tag*).
2. **`develop`**: Rama principal de desarrollo donde se integran todas las nuevas funcionalidades.

# Ramas de Soporte (Temporales)

- **`feature/<nombre-funcionalidad>`**: Creada a partir de `develop`. Se utiliza para desarrollar una nueva característica.
- **`release/<version>`**: Creada a partir de `develop`. Se utiliza para congelar el código, realizar pruebas finales y preparar la entrega.
- **`hotfix/<descripcion>`**: Creada a partir de `main`. Se utiliza para corregir fallos críticos directamente en producción.

---

# Guía de Comandos GitFlow

# 1. Desarrollar una nueva funcionalidad (Feature)
```bash
# Crear la rama a partir de develop
git checkout develop
git pull origin develop
git checkout -b feature/nueva-funcionalidad

# Desarrollar y realizar commits
git add .
git commit -m "feat: agregar funcionalidad X"

# Subir cambios al repositorio
git push origin feature/nueva-funcionalidad
