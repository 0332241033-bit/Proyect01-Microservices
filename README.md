<div align="center">

# 🚀 Proyect01 — Microservicios con Spring Cloud

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2025.0.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Maven](https://img.shields.io/badge/Maven-3.9.11-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-latest-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

**Un proyecto educativo completo de arquitectura de microservicios** implementado con Spring Cloud, demostrando patrones clave como Service Discovery, API Gateway, configuración centralizada y comunicación entre servicios.

[📖 Guía de Instalación](#-instalación-y-configuración) · [🗺️ Arquitectura](#️-arquitectura-del-sistema) · [📡 API Reference](docs/api-reference.md) · [🤝 Contribuir](docs/contributing.md)

</div>

---

## 📋 Tabla de Contenidos

- [✨ Características](#-características)
- [🗺️ Arquitectura del Sistema](#️-arquitectura-del-sistema)
- [🧩 Microservicios](#-microservicios)
- [🛠️ Stack Tecnológico](#️-stack-tecnológico)
- [⚙️ Prerrequisitos](#️-prerrequisitos)
- [🚀 Instalación y Configuración](#-instalación-y-configuración)
- [▶️ Orden de Inicio](#️-orden-de-inicio)
- [📡 Endpoints de la API](#-endpoints-de-la-api)
- [🔗 Comunicación entre Servicios](#-comunicación-entre-servicios)
- [📁 Estructura del Proyecto](#-estructura-del-proyecto)
- [📚 Documentación Adicional](#-documentación-adicional)
- [🤝 Contribuir](#-contribuir)

---

## ✨ Características

| Característica | Descripción |
|---|---|
| 🔍 **Service Discovery** | Registro y descubrimiento automático de servicios con **Netflix Eureka** |
| 🚪 **API Gateway** | Punto de entrada único con enrutamiento inteligente via **Spring Cloud Gateway** |
| ⚙️ **Config Server** | Gestión centralizada de configuración con **Spring Cloud Config** |
| 🔗 **Inter-Service Calls** | Comunicación declarativa entre servicios usando **OpenFeign** |
| 🗄️ **Bases de Datos Independientes** | Cada servicio con su propia base de datos (MySQL y PostgreSQL) |
| 📊 **Health Monitoring** | Monitoreo de salud via **Spring Boot Actuator** |
| 🧩 **Arquitectura Modular** | Separación clara de responsabilidades por dominio |

---

## 🗺️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENTE (HTTP)                              │
│                    Postman / Browser / App                          │
└───────────────────────────┬─────────────────────────────────────────┘
                            │ Todas las peticiones entran por aquí
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    🚪 API GATEWAY                                   │
│                     Puerto: 8080                                    │
│                                                                     │
│   /api/student/**  ──────────►  http://localhost:8090              │
│   /api/course/**   ──────────►  http://localhost:9090              │
│                                                                     │
└─────────────────┬────────────────────┬──────────────────────────────┘
                  │                    │
                  ▼                    ▼
┌─────────────────────────┐  ┌─────────────────────────┐
│   👨‍🎓 STUDENT SERVICE   │  │    📚 COURSE SERVICE     │
│     Puerto: 8090        │  │      Puerto: 9090        │
│                         │  │                          │
│   ┌─────────────────┐   │  │   ┌─────────────────┐   │
│   │  MySQL DB       │   │  │   │  PostgreSQL DB   │   │
│   │  students_db    │   │  │   │   course_db      │   │
│   │  Puerto: 3306   │   │  │   │   Puerto: 5432   │   │
│   └─────────────────┘   │  │   └─────────────────┘   │
└─────────────────────────┘  └────────────┬────────────┘
         ▲                                │
         │    Feign Client (HTTP REST)     │
         └────────────────────────────────┘
              Course llama a Student
              para obtener lista de alumnos

         ┌─────────────────────────────────────────┐
         │         Todos los servicios registran   │
         │              y descubren servicios      │
         ▼                                         ▼
┌─────────────────────────┐  ┌─────────────────────────┐
│   🔍 EUREKA SERVER      │  │   ⚙️ CONFIG SERVER       │
│     Puerto: 8761        │  │     Puerto: 8888         │
│   Service Discovery     │  │  Configuración Central  │
└─────────────────────────┘  └─────────────────────────┘
```

### 🔄 Flujo de una Petición

```
1. Cliente envía GET http://localhost:8080/api/course/search_students/1
                                │
2. API Gateway recibe y enruta  │
   → /api/course/**             ▼
                        Course Service (9090)
                                │
3. Course Service consulta      │
   su base de datos PostgreSQL  │
   para obtener info del curso  │
                                │
4. Course Service llama vía     │
   Feign a Student Service:     ▼
                        Student Service (8090)
                                │
5. Student Service consulta     │
   MySQL y retorna alumnos      │
   del curso                    │
                                ▼
6. Course Service combina    Respuesta final
   datos y responde          al cliente
```

---

## 🧩 Microservicios

### 1. ⚙️ Config Server (`microservices.config`)

> Servidor de configuración centralizada que provee propiedades a todos los microservicios al inicio.

| Propiedad | Valor |
|---|---|
| **Puerto** | `8888` |
| **Nombre** | `microservices.config` |
| **Función** | Centralizar la configuración de todos los servicios |
| **Tecnología** | Spring Cloud Config Server |

**Ventaja clave:** Permite cambiar configuraciones sin redesplegar los servicios.

---

### 2. 🔍 Eureka Server (`microservices.eureka`)

> Servidor de registro y descubrimiento de servicios. Todos los microservicios se registran aquí al arrancar.

| Propiedad | Valor |
|---|---|
| **Puerto** | `8761` |
| **Nombre** | `msvc-eureka` |
| **Función** | Service Registry + Service Discovery |
| **Dashboard** | `http://localhost:8761` |

**¿Por qué Eureka?** Permite escalar servicios horizontalmente sin cambiar URLs hardcodeadas. Los servicios se encuentran por nombre, no por IP.

```yaml
# Configuración Eureka Server
eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false   # No se registra a sí mismo
    fetch-registry: false         # No busca otros servicios
```

---

### 3. 🚪 API Gateway (`microservices.gateway`)

> Punto de entrada único para todos los clientes. Enruta las peticiones al servicio correcto basándose en el path.

| Propiedad | Valor |
|---|---|
| **Puerto** | `8080` |
| **Nombre** | `msvc-gateway` |
| **Función** | Enrutamiento, load balancing |
| **Tecnología** | Spring Cloud Gateway (WebMvc) |

**Rutas configuradas:**

```yaml
routes:
  - id: students
    uri: http://localhost:8090        # Servicio de Estudiantes
    predicates:
      - Path=/api/student/**          # Cualquier path que empiece con /api/student/

  - id: courses
    uri: http://localhost:9090        # Servicio de Cursos
    predicates:
      - Path=/api/course/**           # Cualquier path que empiece con /api/course/
```

---

### 4. 👨‍🎓 Student Service (`microservices.student`)

> Microservicio responsable de la gestión completa de estudiantes.

| Propiedad | Valor |
|---|---|
| **Puerto** | `8090` |
| **Nombre** | `msvc-student` |
| **Base de Datos** | MySQL 8 (`students_db`) |
| **DB Puerto** | `3306` |

**Modelo de datos:**

```
┌─────────────────────────────┐
│         students            │
├─────────────────────────────┤
│ id          BIGINT (PK, AI) │
│ name        VARCHAR         │
│ last_name   VARCHAR         │
│ email       VARCHAR         │
│ course_id   BIGINT (FK)     │
└─────────────────────────────┘
```

**Endpoints disponibles:**

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/student/create` | Crear nuevo estudiante |
| `GET` | `/api/student/all` | Listar todos los estudiantes |
| `GET` | `/api/student/search/{id}` | Buscar estudiante por ID |
| `GET` | `/api/student/serach_by_id_course/{idCourse}` | Buscar estudiantes por curso |

---

### 5. 📚 Course Service (`microservices.course`)

> Microservicio responsable de la gestión de cursos. También orquesta la obtención de estudiantes de un curso llamando al Student Service.

| Propiedad | Valor |
|---|---|
| **Puerto** | `9090` |
| **Nombre** | `msvc-course` |
| **Base de Datos** | PostgreSQL (`course_db`) |
| **DB Puerto** | `5432` |

**Modelo de datos:**

```
┌─────────────────────────────┐
│          courses            │
├─────────────────────────────┤
│ id          BIGINT (PK, AI) │
│ name        VARCHAR         │
│ teacher     VARCHAR         │
└─────────────────────────────┘
```

**Endpoints disponibles:**

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/course/create` | Crear nuevo curso |
| `GET` | `/api/course/all` | Listar todos los cursos |
| `GET` | `/api/course/search/{id}` | Buscar curso por ID |
| `GET` | `/api/course/search_students/{idCourse}` | Ver curso con sus estudiantes |

**Respuesta agregada de `/search_students/{idCourse}`:**

```json
{
  "courseName": "Arquitectura de Software",
  "teacher": "Dr. García",
  "studentDTOList": [
    {
      "name": "Juan",
      "lastname": "Pérez",
      "email": "juan@email.com",
      "courseId": 1
    }
  ]
}
```

---

## 🛠️ Stack Tecnológico

| Categoría | Tecnología | Versión | Propósito |
|---|---|---|---|
| **Lenguaje** | Java | 17 (LTS) | Lenguaje base del proyecto |
| **Framework Core** | Spring Boot | 3.5.6 | Framework principal de aplicación |
| **Microservicios** | Spring Cloud | 2025.0.0 | Ecosistema de microservicios |
| **Service Discovery** | Netflix Eureka | (Spring Cloud) | Registro y descubrimiento |
| **API Gateway** | Spring Cloud Gateway | WebMvc | Enrutamiento centralizado |
| **Config** | Spring Cloud Config | (Spring Cloud) | Configuración centralizada |
| **HTTP Client** | OpenFeign | (Spring Cloud) | Comunicación entre servicios |
| **ORM** | Spring Data JPA / Hibernate | (Spring Boot) | Acceso a datos |
| **DB Students** | MySQL | 8.x | Persistencia de estudiantes |
| **DB Courses** | PostgreSQL | Latest | Persistencia de cursos |
| **Build** | Apache Maven | 3.9.11 | Gestión de dependencias y build |
| **Boilerplate** | Lombok | 1.18.40 | Reducción de código repetitivo |
| **Monitoring** | Spring Boot Actuator | (Spring Boot) | Health checks y métricas |
| **Testing** | Spring Boot Test | (Spring Boot) | Framework de pruebas |

---

## ⚙️ Prerrequisitos

Antes de empezar, asegúrate de tener instalado:

| Herramienta | Versión Mínima | Verificar con |
|---|---|---|
| **Java JDK** | 17 | `java -version` |
| **Apache Maven** | 3.8+ | `mvn -version` |
| **MySQL Server** | 8.0+ | `mysql --version` |
| **PostgreSQL** | 12+ | `psql --version` |
| **Git** | 2.x | `git --version` |

> 💡 **Consejo:** Puedes usar el Maven Wrapper (`./mvnw`) incluido en el proyecto si no tienes Maven instalado globalmente.

### 🐧 Instalación rápida en Linux/Mac

```bash
# Java 17 (Ubuntu/Debian)
sudo apt update && sudo apt install openjdk-17-jdk

# MySQL
sudo apt install mysql-server

# PostgreSQL
sudo apt install postgresql postgresql-contrib
```

### 🪟 Instalación en Windows

Descarga los instaladores oficiales:
- **Java 17:** [Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java17) o [OpenJDK](https://adoptium.net/)
- **MySQL:** [MySQL Installer](https://dev.mysql.com/downloads/installer/)
- **PostgreSQL:** [PostgreSQL Downloads](https://www.postgresql.org/download/windows/)

---

## 🚀 Instalación y Configuración

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/0332241033-bit/Proyect01-Microservices.git
cd Proyect01-Microservices
```

### Paso 2: Configurar las Bases de Datos

#### MySQL (para Student Service)

```sql
-- Conectarse a MySQL
mysql -u root -p

-- Crear base de datos
CREATE DATABASE students_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verificar
SHOW DATABASES;
```

#### PostgreSQL (para Course Service)

```bash
# Conectarse a PostgreSQL
sudo -u postgres psql

# Crear base de datos
CREATE DATABASE course_db;

# Salir
\q
```

### Paso 3: Configurar Credenciales

> ⚠️ **Importante:** Los archivos de configuración traen credenciales de ejemplo. Actualiza según tu entorno.

**Student Service** (`microservices.student/src/main/resources/application.yml`):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/students_db
    username: tu_usuario_mysql       # ← Cambiar
    password: tu_contraseña_mysql    # ← Cambiar
```

**Course Service** (`microservices.course/src/main/resources/application.yml`):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/course_db
    username: postgres               # ← Cambiar si es necesario
    password: tu_contraseña_pg       # ← Cambiar
```

### Paso 4: Compilar el Proyecto

```bash
# Compilar todos los módulos desde la raíz
mvn clean install -DskipTests

# O usando el Maven Wrapper
./mvnw clean install -DskipTests
```

Resultado esperado:
```
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  XX.XXX s
```

---

## ▶️ Orden de Inicio

> ⚡ **Crítico:** Los servicios deben iniciarse en este orden exacto para garantizar correcto funcionamiento.

```
1️⃣  Config Server    (8888)  →  Debe estar listo PRIMERO
2️⃣  Eureka Server    (8761)  →  Se conecta al Config Server
3️⃣  API Gateway      (8080)  →  Necesita Eureka para enrutar
4️⃣  Student Service  (8090)  →  Servicios de negocio
5️⃣  Course Service   (9090)  →  Servicios de negocio (usa Student)
```

### Comandos de Inicio (una terminal por servicio)

```bash
# Terminal 1 — Config Server
cd microservices.config
mvn spring-boot:run

# Terminal 2 — Eureka Server (esperar a que Config Server inicie)
cd microservices.eureka
mvn spring-boot:run

# Terminal 3 — API Gateway
cd microservices.gateway
mvn spring-boot:run

# Terminal 4 — Student Service
cd microservices.student
mvn spring-boot:run

# Terminal 5 — Course Service
cd microservices.course
mvn spring-boot:run
```

### Verificación del Sistema

Una vez todos iniciados, accede al **Dashboard de Eureka** para verificar que todos los servicios estén registrados:

```
http://localhost:8761
```

Deberías ver registrados:
- `MSVC-GATEWAY`
- `MSVC-STUDENT`
- `MSVC-COURSE`

---

## 📡 Endpoints de la API

> Todas las peticiones deben realizarse a través del **API Gateway** en `http://localhost:8080`

### 👨‍🎓 Estudiantes

#### Crear Estudiante
```http
POST http://localhost:8080/api/student/create
Content-Type: application/json

{
  "name": "Juan",
  "lastname": "Pérez García",
  "email": "juan.perez@university.edu",
  "courseId": 1
}
```

**Respuesta:** `201 Created`

---

#### Obtener Todos los Estudiantes
```http
GET http://localhost:8080/api/student/all
```

**Respuesta exitosa:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Juan",
    "lastname": "Pérez García",
    "email": "juan.perez@university.edu",
    "courseId": 1
  }
]
```
**Sin contenido:** `204 No Content`

---

#### Buscar Estudiante por ID
```http
GET http://localhost:8080/api/student/search/1
```

**Respuesta exitosa:** `200 OK`
```json
{
  "id": 1,
  "name": "Juan",
  "lastname": "Pérez García",
  "email": "juan.perez@university.edu",
  "courseId": 1
}
```
**No encontrado:** `404 Not Found`

---

#### Buscar Estudiantes por Curso
```http
GET http://localhost:8080/api/student/serach_by_id_course/1
```

**Respuesta:** `200 OK` — Lista de estudiantes inscritos en el curso indicado.

---

### 📚 Cursos

#### Crear Curso
```http
POST http://localhost:8080/api/course/create
Content-Type: application/json

{
  "name": "Arquitectura de Software",
  "teacher": "Dr. García López"
}
```

**Respuesta:** `201 Created`

---

#### Obtener Todos los Cursos
```http
GET http://localhost:8080/api/course/all
```

**Respuesta exitosa:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Arquitectura de Software",
    "teacher": "Dr. García López"
  }
]
```

---

#### Buscar Curso con sus Estudiantes
```http
GET http://localhost:8080/api/course/search_students/1
```

**Respuesta:** `200 OK`
```json
{
  "courseName": "Arquitectura de Software",
  "teacher": "Dr. García López",
  "studentDTOList": [
    {
      "name": "Juan",
      "lastname": "Pérez García",
      "email": "juan.perez@university.edu",
      "courseId": 1
    },
    {
      "name": "María",
      "lastname": "González",
      "email": "maria.gonzalez@university.edu",
      "courseId": 1
    }
  ]
}
```

---

## 🔗 Comunicación entre Servicios

### Feign Client — Course → Student

El Course Service usa **OpenFeign** para llamar al Student Service de forma declarativa:

```java
@FeignClient(name = "msvc-student", url = "localhost:8080/api/student")
public interface studentClient {

    @GetMapping("/serach_by_id_course/{idCourse}")
    List<studentDTO> findAllStudentByCourse(@PathVariable Long idCourse);
}
```

**¿Cómo funciona Feign?**
1. Se declara una interfaz anotada con `@FeignClient`
2. Se definen métodos con las mismas anotaciones que un `@RestController`
3. Spring genera automáticamente el cliente HTTP en tiempo de arranque
4. No se necesita código de cliente HTTP manual (`RestTemplate`, `WebClient`, etc.)

### Flujo de `/api/course/search_students/{id}`

```
Cliente
  │
  │ GET /api/course/search_students/1
  ▼
API Gateway (8080)
  │
  │ Reenvía a /api/course/search_students/1
  ▼
Course Service (9090)
  │
  ├─ Busca curso en PostgreSQL → { id:1, name:"Arquitectura", teacher:"Dr. García" }
  │
  ├─ Llama via Feign: GET http://localhost:8080/api/student/serach_by_id_course/1
  │                              │
  │                              ▼
  │                       API Gateway (8080)
  │                              │
  │                              ▼
  │                       Student Service (8090)
  │                              │
  │                       Busca en MySQL → [{ name:"Juan", ... }]
  │                              │
  │                       ◄──────┘
  │
  ├─ Construye respuesta combinada
  │
  ▼
{ courseName, teacher, studentDTOList: [...] }
```

---

## 📁 Estructura del Proyecto

```
Proyect01-Microservices/
│
├── 📄 pom.xml                          # POM padre (gestión de versiones globales)
│
├── 📂 microservices.config/            # ⚙️ Config Server
│   ├── src/main/java/.../Application.java
│   └── src/main/resources/
│       └── application.properties
│
├── 📂 microservices.eureka/            # 🔍 Eureka Server
│   ├── src/main/java/.../Application.java
│   └── src/main/resources/
│       └── application.yml
│
├── 📂 microservices.gateway/           # 🚪 API Gateway
│   ├── src/main/java/.../Application.java
│   └── src/main/resources/
│       └── application.yml             # Rutas configuradas aquí
│
├── 📂 microservices.student/           # 👨‍🎓 Student Service
│   └── src/main/java/.../
│       ├── Application.java
│       ├── controller/
│       │   └── studentController.java  # Endpoints REST
│       ├── entity/
│       │   └── student.java            # Entidad JPA
│       ├── persistence/
│       │   └── studentRepository.java  # Repositorio (CrudRepository)
│       └── service/
│           ├── IstudetnService.java    # Interfaz del servicio
│           └── studentServiceImpl.java # Implementación
│
└── 📂 microservices.course/            # 📚 Course Service
    └── src/main/java/.../
        ├── Application.java
        ├── client/
        │   └── studentClient.java      # Feign Client → Student Service
        ├── controller/
        │   ├── courseController.java   # Endpoints REST
        │   └── dto/
        │       └── studentDTO.java     # DTO para datos de estudiante
        ├── entity/
        │   └── course.java             # Entidad JPA
        ├── http/response/
        │   └── studentByCourseResponse.java  # Respuesta agregada
        ├── persistence/
        │   └── IcourseRepository.java  # Repositorio (CrudRepository)
        └── service/
            ├── IcourseService.java     # Interfaz del servicio
            └── courseServiceImpl.java  # Implementación (orquestación)
```

---

## 🩺 Monitoreo y Health Checks

Todos los servicios exponen endpoints de Actuator para monitoreo:

| Servicio | Health Endpoint |
|---|---|
| Config Server | `http://localhost:8888/actuator/health` |
| Eureka Server | `http://localhost:8761/actuator/health` |
| API Gateway | `http://localhost:8080/actuator/health` |
| Student Service | `http://localhost:8090/actuator/health` |
| Course Service | `http://localhost:9090/actuator/health` |

**Respuesta esperada:**
```json
{
  "status": "UP"
}
```

---

## 📚 Documentación Adicional

| Documento | Descripción |
|---|---|
| [🗺️ Arquitectura Detallada](docs/architecture.md) | Diagramas y patrones de diseño explicados |
| [📡 API Reference Completa](docs/api-reference.md) | Documentación detallada de todos los endpoints |
| [🚀 Guía de Setup](docs/setup-guide.md) | Instalación paso a paso con solución de problemas |
| [🤝 Guía de Contribución](docs/contributing.md) | Cómo contribuir al proyecto |

---

## ⚠️ Notas Importantes

> **Este es un proyecto educativo.** Las siguientes configuraciones son apropiadas para aprender, pero deben ser mejoradas para entornos de producción:

1. **Credenciales hardcodeadas** — En producción usar variables de entorno o un vault de secretos
2. **`ddl-auto: create`** — Recreará las tablas en cada reinicio (pérdida de datos). Usar `update` o `validate` en producción
3. **URL fija en Feign** — El client Feign usa URL hardcodeada; en producción usar service discovery por nombre
4. **Sin autenticación** — Los endpoints no tienen seguridad. Considerar Spring Security + JWT

---

## 🤝 Contribuir

¿Quieres mejorar este proyecto? ¡Las contribuciones son bienvenidas!

1. Haz un **Fork** del repositorio
2. Crea una rama: `git checkout -b feature/mi-mejora`
3. Realiza tus cambios y haz commit: `git commit -m 'feat: agregar mi mejora'`
4. Push a tu rama: `git push origin feature/mi-mejora`
5. Abre un **Pull Request**

Lee la [Guía de Contribución](docs/contributing.md) para más detalles.

---

## 📄 Licencia

Este proyecto es de código abierto creado con fines educativos.

---

<div align="center">

Hecho con ❤️ para aprender microservicios con Spring Cloud

**¿Encontraste un bug o tienes una sugerencia?** [Abre un issue](https://github.com/0332241033-bit/Proyect01-Microservices/issues)

</div>
