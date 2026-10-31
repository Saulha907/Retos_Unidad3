# Cine Las Palmas — Reto 1 corregido
Proyecto Spring Boot REST (Java 21, Maven, Spring Data JPA, H2) a partir de los archivos originales.

## Ejecutar
Abrir esta carpeta (la que contiene `pom.xml`) en IntelliJ IDEA, Eclipse/STS o VS Code con extensiones Java. Con Java 21 y Maven instalados, ejecutar `mvn spring-boot:run` o ejecutar `CineApplication.java`.
API en `http://localhost:8080`. H2 persiste los datos en `./datos/cine` (carpeta que se crea al ejecutar).

## Pruebas en Postman (Body -> raw -> JSON)
1. POST `http://localhost:8080/peliculas`: `{ "titulo":"Aventura en el mar", "clasificacion":"B", "duracion":120, "genero":"Aventura" }`
2. POST `http://localhost:8080/salas`: `{ "numero":1, "capacidad":60 }` (salas con número del 1 al 6).
3. POST `http://localhost:8080/clientes`: `{ "nombre":"Ana García", "email":"ana@example.com" }`
4. POST `http://localhost:8080/funciones?peliculaId=1&salaId=1`: `{ "fechaHora":"2026-11-15T18:00:00", "precio":100 }`
5. POST `http://localhost:8080/boletos?funcionId=1&clienteId=1&fila=A&numero=1` (sin body).
6. GET `http://localhost:8080/boletos/funcion/1` y GET `/clientes` (consultar puntos).
7. GET `/peliculas?clasificacion=B`, GET `/peliculas/1`, GET `/salas`, GET `/funciones`, GET `/funciones/1`.

## Restricciones y notas
- Clasificaciones válidas: A, B, B15, C; duración y precio positivos.
- No se permite programar funciones traslapadas en una misma sala.
- No se permite vender el mismo asiento dos veces para la misma función; número de asiento entre 1 y capacidad de sala (validación simplificada, sin plano por filas).
- Los clientes registrados acumulan `floor(precio/10)` puntos por boleto; una venta sin cliente también es válida.
- JSON anidado: GET `/funciones/1` devuelve `pelicula` y `sala` como objetos.
- No se incluye XML por indicación del estudiante.
- El proyecto está diseñado para práctica local, no para producción con ventas concurrentes ni integración de pagos.
