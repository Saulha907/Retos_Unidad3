# Reto 2 — Alianza «Alas del Sur»

Proyecto de Arquitectura Orientada a Servicios (Unidad 3, Semántica). Integra un modelo canónico de reservas para **AeroNorte**, **SurJet** y **CaribeAir**.

## Requisitos
- Java 21 y Maven 3.9+ para la API REST Spring Boot.
- Python 3 (opcional para validar archivos académicos), con `jsonschema` y `lxml`.

## Cómo ejecutarlo en IntelliJ / Eclipse / Spring Tool Suite
1. Descomprime el ZIP y abre la carpeta del proyecto que contiene `pom.xml`.
2. Espera a que se descarguen dependencias Maven.
3. Ejecuta `src/main/java/com/api/alianza/AlianzaApplication.java`.
4. La API escucha en `http://localhost:8080`.

Alternativa: `mvn spring-boot:run`.

## Endpoints y Postman
- `GET http://localhost:8080/api/v1/reservas/muestras/aeronorte`
- `GET http://localhost:8080/api/v1/reservas/muestras/surjet`
- `GET http://localhost:8080/api/v1/reservas/muestras/caribeair`
- `POST http://localhost:8080/api/v1/reservas/normalizar/aeronorte` (Body > raw > JSON, copiar el ejemplo original).
- También se admite POST con las otras dos aerolíneas. Un código de aerolínea desconocido retorna HTTP 400.

## Contenido de la entrega
- `src/main/resources/muestras/originales/*.json`: muestras del caso. AeroNorte se transcribió de XML de la guía a JSON para la API.
- `datos-transformados/*.json`: tres reservas canónicas.
- `datos-transformados/*.xml`: las mismas reservas para evaluar el esquema XML.
- `esquemas/reserva.schema.json`: esquema JSON Schema (Draft 2020-12).
- `esquemas/alianza.xsd`: esquema XSD con restricciones y `xs:extension`.
- `validar_ejemplos.py`: ejecución de las seis validaciones.
- `documentacion/`: diagrama UML y glosario.

## Validación académica
Ejecuta `pip install jsonschema lxml` y luego `python validar_ejemplos.py` desde la raíz del proyecto.

## Decisiones semánticas importantes
- `idGlobal` es un UUID v3 reproducible a partir de `ALAS-DEL-SUR:<aerolinea>:<idOrigen>`; los identificadores locales **no** se mezclan.
- No se convierte USD a MXN sin una tasa de cambio y fecha documentadas.
- Una pieza de CaribeAir indica **máximo de 23 kg**, no un peso real medido.
- Se preserva la fecha de AeroNorte y CaribeAir **sin hora**; nunca se inventa 08:30.
- La fuente AeroNorte no proporciona acentos; en la reserva de AeroNorte se conserva `Garcia` en lugar de atribuir un acento inexistente. SurJet conserva `García`.
- En AeroNorte, `menor=N` se clasifica como `ADULTO` siguiendo el límite de 12 años documentado; se indica expresamente que no se conoce su edad exacta.
- Las muestras no incluyen número, origen ni destino de vuelo, por eso `vuelos` es un arreglo vacío y no se inventan datos.

**Nota:** los archivos XML/XSD se incluyen porque son criterios explícitos del Reto 2. El funcionamiento de la API REST utiliza JSON.
