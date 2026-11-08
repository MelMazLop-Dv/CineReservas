
# CineReservas

## 1. Información general

**Nombre del proyecto:** CineReservas

**Nombre completo del estudiante:** Melvin Apolonio Mazariegos López

**Número de carné:** 9941 25 25174

**Curso:** Programación II

**Tipo de proyecto:** Aplicación web para la administración de funciones cinematográficas y reservas de entradas.

---

## 2. Descripción del problema

Los cines independientes necesitan administrar de manera eficiente sus funciones cinematográficas y las reservas de entradas realizadas por sus clientes.

Cuando estos procesos se realizan manualmente, pueden presentarse dificultades para controlar la disponibilidad de asientos, registrar las reservas y mantener actualizada la información de cada función.

Uno de los principales problemas es la posibilidad de registrar más reservas que la capacidad disponible de una sala, lo que ocasionaría sobreventa de entradas. También pueden producirse errores al calcular el precio total de las reservas o al actualizar la cantidad de asientos disponibles después de una cancelación.

Para solucionar esta problemática se desarrolló **CineReservas**, una aplicación web que permite registrar y administrar funciones de cine, gestionar reservas y controlar automáticamente la disponibilidad de asientos.

El sistema utiliza un backend desarrollado con Java y Spring Boot, una base de datos PostgreSQL y una interfaz web construida con HTML, CSS y JavaScript.

### Objetivo general

Desarrollar una aplicación web para administrar funciones cinematográficas y reservas de entradas, garantizando el control de la capacidad de las salas y evitando la sobreventa de asientos.

### Objetivos específicos

1. Implementar operaciones CRUD para administrar las funciones cinematográficas.
2. Registrar, consultar, actualizar y cancelar reservas.
3. Controlar automáticamente la disponibilidad de asientos.
4. Calcular el precio total de las entradas reservadas.
5. Implementar filtros de consulta e indicadores de ocupación.
6. Desarrollar una interfaz web que consuma los servicios REST del backend.

---

## 3. Tecnologías utilizadas

### Backend

- Java 21.
- Spring Boot 4.1.2.
- Spring Web.
- Spring Data JPA.
- Jakarta Validation.
- Maven.

### Base de datos

- PostgreSQL 18.
- pgAdmin 4.

### Frontend

- HTML5.
- CSS3.
- JavaScript.
- Fetch API.

### Herramientas de desarrollo

- IntelliJ IDEA.
- Postman.
- Git.
- GitHub.

---

## 4. Estructura general del sistema

CineReservas utiliza una arquitectura por capas que permite separar las responsabilidades del sistema.

### Capa de presentación

Está desarrollada con HTML, CSS y JavaScript.

Permite al usuario:

- Visualizar funciones cinematográficas.
- Registrar nuevas funciones.
- Modificar y eliminar funciones.
- Consultar la disponibilidad de asientos.
- Registrar reservas.
- Actualizar y cancelar reservas.
- Consultar indicadores del sistema.

### Capa de controladores

Los controladores reciben las solicitudes HTTP y devuelven las respuestas de la API REST.

Controladores principales:

- FuncionController.
- ReservaController.

### Capa de servicios

Contiene la lógica de negocio.

Servicios principales:

- FuncionService.
- ReservaService.

Entre sus responsabilidades están validar reservas, actualizar los asientos disponibles y calcular los importes correspondientes.

### Capa de repositorios

Utiliza Spring Data JPA para realizar operaciones de acceso a la base de datos.

Repositorios principales:

- FuncionRepository.
- ReservaRepository.

### Capa de entidades

Representa las tablas principales de la base de datos:

**Funcion:** almacena la información de cada función cinematográfica.

**Reserva:** almacena las reservas realizadas para una función.

### Relación entre entidades

Una función puede tener muchas reservas.

Cada reserva pertenece a una única función.

La relación es de uno a muchos (1:N).

### Estructura de carpetas

```text
reservas/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/cine/reservas/
│       │       ├── controller/
│       │       ├── service/
│       │       ├── repository/
│       │       ├── entity/
│       │       ├── dto/
│       │       └── exception/
│       └── resources/
│           ├── static/
│           │   ├── index.html
│           │   ├── styles.css
│           │   └── app.js
│           └── application.properties
├── database/
│   ├── creacion_bd.sql
│   └── datos_prueba.sql
├── postman/
│   └── CineReservas.postman_collection.json
├── pom.xml
└── README.md
```

---

## 5. Requisitos para ejecutar el proyecto

Para ejecutar CineReservas es necesario contar con:

1. Java JDK 21 instalado.
2. PostgreSQL instalado y funcionando.
3. Maven o Maven Wrapper.
4. IntelliJ IDEA u otro entorno de desarrollo compatible.
5. Un navegador web actualizado.
6. Acceso a una base de datos PostgreSQL.
7. La contraseña de PostgreSQL configurada mediante una variable de entorno.

El backend utiliza el puerto 8080 y PostgreSQL utiliza el puerto 5432 en la configuración local del proyecto.

---

## 6. Configuración de PostgreSQL

### Paso 1. Abrir pgAdmin 4

Iniciar pgAdmin y conectarse al servidor PostgreSQL.

### Paso 2. Crear la base de datos

Crear una base de datos con el nombre:

`cine_reservas`

También puede utilizarse la instrucción SQL:

```sql
CREATE DATABASE cine_reservas;
```

### Paso 3. Configurar las tablas

El proyecto incluye el archivo:

`database/creacion_bd.sql`

Este archivo contiene las instrucciones SQL para crear las tablas principales del sistema.

Las tablas son:

- funciones
- reservas

También se incluye:

`database/datos_prueba.sql`

Este archivo contiene registros de ejemplo.

Los scripts deben utilizarse sobre una base de datos nueva para evitar registros duplicados.

### Paso 4. Configurar application.properties

Abrir el archivo:

`src/main/resources/application.properties`

La configuración principal es:

```properties
spring.application.name=reservas

server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/cine_reservas
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

La variable de entorno `DB_PASSWORD` debe contener la contraseña local del usuario de PostgreSQL.

Por seguridad, no se deben publicar contraseñas reales en GitHub.

---

## 7. Instrucciones para iniciar el backend

### Paso 1. Obtener el proyecto

Clonar el repositorio desde GitHub:

```bash
git clone URL_DEL_REPOSITORIO
```

Reemplazar `URL_DEL_REPOSITORIO` por la dirección real del repositorio.

### Paso 2. Abrir IntelliJ IDEA

Seleccionar la opción Open y abrir la carpeta del proyecto.

Esperar a que Maven descargue las dependencias necesarias.

### Paso 3. Configurar la conexión

Verificar que PostgreSQL esté ejecutándose y que exista la base de datos `cine_reservas`.

Configurar la variable de entorno `DB_PASSWORD` con la contraseña correspondiente.

En IntelliJ IDEA puede configurarse desde:

Run > Edit Configurations > Environment variables

### Paso 4. Ejecutar Spring Boot

Buscar la clase principal:

`ReservasApplication`

Ejecutarla utilizando el botón Run de IntelliJ IDEA.

También es posible iniciar el proyecto mediante Maven:

```bash
mvn spring-boot:run
```

### Paso 5. Verificar el backend

Abrir un navegador y visitar:

http://localhost:8080/api/funciones

Si el backend funciona correctamente, devolverá una respuesta JSON con las funciones registradas.

---

## 8. Instrucciones para abrir el frontend

El frontend se encuentra dentro de:

`src/main/resources/static/`

Los archivos principales son:

- index.html
- styles.css
- app.js

### Procedimiento

1. Iniciar PostgreSQL.
2. Ejecutar el backend desde IntelliJ IDEA.
3. Esperar a que Spring Boot termine de iniciar.
4. Abrir un navegador web.
5. Ingresar a:

http://localhost:8080/

La aplicación mostrará la interfaz principal de CineReservas.

Desde esta interfaz se pueden administrar funciones, registrar reservas, consultar información y visualizar indicadores.

El frontend utiliza Fetch API para comunicarse con los endpoints REST del backend.

---

## 9. Descripción de los endpoints

La API REST permite realizar operaciones sobre funciones cinematográficas y reservas.

### Endpoints de funciones

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/funciones | Consultar todas las funciones. |
| GET | /api/funciones/{id} | Consultar una función por ID. |
| POST | /api/funciones | Registrar una nueva función. |
| PUT | /api/funciones/{id} | Actualizar una función existente. |
| DELETE | /api/funciones/{id} | Eliminar una función cuando sea permitido. |
| GET | /api/funciones/fecha?fecha=YYYY-MM-DD | Consultar funciones por fecha. |
| GET | /api/funciones/disponibles | Consultar funciones con asientos disponibles. |
| GET | /api/funciones/{id}/ocupacion | Consultar el porcentaje de ocupación. |

### Endpoints de reservas

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/reservas | Consultar todas las reservas. |
| GET | /api/reservas/{id} | Consultar una reserva por ID. |
| POST | /api/reservas | Registrar una nueva reserva. |
| PUT | /api/reservas/{id} | Actualizar una reserva existente. |
| PATCH | /api/reservas/{id}/cancelar | Cancelar una reserva. |
| GET | /api/reservas?funcionId={id} | Consultar reservas de una función. |
| GET | /api/reservas?estado=CONFIRMADA | Consultar reservas según su estado. |

### Ejemplo de registro de función

Método: POST

Endpoint:

`/api/funciones`

```json
{
  "pelicula": "Avatar 3",
  "fecha": "2026-12-15",
  "hora": "18:00:00",
  "sala": "Sala 4",
  "capacidad": 60,
  "precioEntrada": 45.00
}
```

### Ejemplo de registro de reserva

Método: POST

Endpoint:

`/api/reservas`

```json
{
  "funcionId": 1,
  "nombreCliente": "Pedro Ramirez",
  "cantidadEntradas": 2
}
```

El sistema calcula el precio total y actualiza los asientos disponibles.

### Códigos de respuesta HTTP

La API utiliza códigos HTTP para comunicar los resultados:

- 200 OK: operación de consulta o actualización exitosa.
- 201 Created: recurso creado, cuando el controlador devuelve este estado.
- 400 Bad Request: datos inválidos.
- 404 Not Found: recurso inexistente.
- 409 Conflict: operación que incumple una regla de negocio.

---

## 10. Reglas principales del negocio

El sistema implementa las siguientes reglas:

### Regla 1. Cantidad positiva

La cantidad de entradas de una reserva debe ser mayor que cero.

### Regla 2. Disponibilidad de asientos

No se permite reservar más entradas que los asientos disponibles de una función.

### Regla 3. Actualización de disponibilidad

Al registrar una reserva confirmada, se descuentan automáticamente los asientos correspondientes.

### Regla 4. Cancelación de reservas

Al cancelar una reserva confirmada, los asientos reservados vuelven a estar disponibles.

### Regla 5. Cálculo del precio total

El precio total se calcula automáticamente:

**Precio total = Cantidad de entradas × Precio de entrada**

Ejemplo:

Si una entrada cuesta Q40.00 y el cliente reserva 3 entradas:

3 × Q40.00 = Q120.00

### Regla 6. Control de capacidad

No se permite reducir la capacidad de una función por debajo de la cantidad de asientos ocupados.

### Regla 7. Estados de reserva

Las reservas pueden tener dos estados:

- CONFIRMADA
- CANCELADA

### Regla 8. Validación de registros

El sistema comprueba que las funciones y reservas utilizadas en las operaciones existan.

### Regla 9. Prevención de cancelaciones repetidas

Una reserva que ya está cancelada no puede cancelarse nuevamente.

### Regla 10. Integridad de la información

Las operaciones relacionadas con reservas y disponibilidad se realizan mediante servicios y transacciones para mantener la consistencia de los datos.

---

## 11. Pruebas con Postman

El proyecto incluye una colección de Postman para consultar y probar los endpoints REST.

Archivo:

`postman/CineReservas.postman_collection.json`

### Importar la colección

1. Abrir Postman.
2. Seleccionar Import.
3. Buscar el archivo JSON del proyecto.
4. Importar la colección.
5. Iniciar el backend.
6. Ejecutar las peticiones necesarias.

Las pruebas permiten verificar:

- Registro de funciones.
- Consulta y modificación de funciones.
- Registro de reservas.
- Control de asientos disponibles.
- Actualización de reservas.
- Cancelación de reservas.
- Filtros de consulta.
- Porcentaje de ocupación.
- Validaciones y manejo de errores.

---

## 12. Conclusión

CineReservas es una aplicación web que integra conocimientos de programación orientada a objetos, desarrollo de APIs REST, bases de datos relacionales y desarrollo frontend.

El sistema permite administrar funciones cinematográficas y reservas de entradas, automatizando el cálculo de precios y el control de asientos disponibles.

Su desarrollo demuestra la aplicación de una arquitectura por capas, validaciones de datos, reglas de negocio y comunicación entre frontend, backend y PostgreSQL.

**Proyecto académico desarrollado para el curso de Programación II.**

**Estudiante:** Melvin Apolonio Mazariegos López

**Carné:** 9941 25 25174
