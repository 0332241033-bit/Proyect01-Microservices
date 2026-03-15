# 🗺️ Arquitectura del Sistema — Proyect01 Microservices

> Documentación técnica detallada sobre los patrones de diseño, decisiones arquitectónicas y flujo de datos del proyecto.

---

## 📋 Tabla de Contenidos

- [Visión General](#visión-general)
- [Patrón de Arquitectura de Microservicios](#patrón-de-arquitectura-de-microservicios)
- [Componentes Detallados](#componentes-detallados)
- [Patrones Implementados](#patrones-implementados)
- [Diagramas de Secuencia](#diagramas-de-secuencia)
- [Modelo de Datos](#modelo-de-datos)
- [Puertos y Redes](#puertos-y-redes)
- [Consideraciones de Diseño](#consideraciones-de-diseño)

---

## Visión General

El sistema implementa una **Arquitectura de Microservicios** basada en Spring Cloud, donde cada componente tiene una responsabilidad única y bien definida (Principio de Responsabilidad Única a nivel de servicio).

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          ECOSISTEMA SPRING CLOUD                                │
│                                                                                 │
│  ┌───────────────┐   ┌───────────────────────────────────────────────────────┐  │
│  │ Config Server │   │           CAPA DE INFRAESTRUCTURA                    │  │
│  │  Puerto 8888  │◄──┤  Todos los servicios obtienen su configuración aquí  │  │
│  └───────────────┘   └───────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ┌───────────────┐   ┌───────────────────────────────────────────────────────┐  │
│  │ Eureka Server │   │             CAPA DE DISCOVERY                        │  │
│  │  Puerto 8761  │◄──┤  Los servicios se registran y descubren aquí         │  │
│  └───────────────┘   └───────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ┌───────────────┐   ┌───────────────────────────────────────────────────────┐  │
│  │  API Gateway  │   │               CAPA DE ACCESO                         │  │
│  │  Puerto 8080  │◄──┤  Único punto de entrada para los clientes            │  │
│  └───────────────┘   └───────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ┌───────────────┐ ┌───────────────┐   ┌─────────────────────────────────────┐  │
│  │Student Service│ │Course Service │   │           CAPA DE NEGOCIO           │  │
│  │  Puerto 8090  │ │  Puerto 9090  │◄──┤  Lógica de negocio y persistencia   │  │
│  └──────┬────────┘ └──────┬────────┘   └─────────────────────────────────────┘  │
│         │                │                                                     │
│   ┌─────▼────┐    ┌──────▼────┐        ┌─────────────────────────────────────┐  │
│   │  MySQL   │    │PostgreSQL │        │           CAPA DE DATOS             │  │
│   │  :3306   │    │  :5432    │◄───────┤  Una base de datos por servicio     │  │
│   └──────────┘    └───────────┘        └─────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## Patrón de Arquitectura de Microservicios

### ¿Por qué Microservicios?

La arquitectura de microservicios ofrece las siguientes ventajas sobre una arquitectura monolítica:

| Aspecto | Monolítico | Microservicios |
|---|---|---|
| **Despliegue** | Todo junto, todo o nada | Cada servicio independientemente |
| **Escalabilidad** | Escala toda la app | Escala solo el servicio que lo necesita |
| **Tecnología** | Una sola tecnología | Cada servicio puede usar la mejor tecnología |
| **Fallos** | Un fallo puede afectar todo | Fallos aislados por servicio |
| **Equipos** | Un equipo grande | Equipos pequeños por servicio |
| **Base de datos** | Una base de datos compartida | Base de datos independiente por servicio |

### Principios SOLID Aplicados

```
S - Single Responsibility: Cada microservicio tiene una sola razón para cambiar
    └─ Student Service: Solo gestiona estudiantes
    └─ Course Service: Solo gestiona cursos

O - Open/Closed: Los servicios exponen interfaces (contratos de API)
    └─ Se pueden agregar nuevos endpoints sin romper los existentes

L - Liskov Substitution: Las implementaciones de servicio respetan las interfaces
    └─ courseServiceImpl implementa IcourseService

I - Interface Segregation: Interfaces específicas por servicio
    └─ IstudetnService, IcourseService

D - Dependency Inversion: Los controladores dependen de interfaces, no implementaciones
    └─ courseController usa IcourseService, no courseServiceImpl directamente
```

---

## Componentes Detallados

### 1. ⚙️ Spring Cloud Config Server

**Rol:** Externalización y centralización de la configuración.

```
┌─────────────────────────────────────────────┐
│             Config Server                   │
│                                             │
│  ┌──────────────────────────────────────┐   │
│  │ @EnableConfigServer                  │   │
│  │                                      │   │
│  │  Fuente de configuración:            │   │
│  │  - Classpath (archivos locales)      │   │
│  │  - Git Repository (producción)       │   │
│  │  - Vault (secretos)                  │   │
│  └──────────────────────────────────────┘   │
│                                             │
│  Expone: /[app-name]/[profile]              │
│  Ejemplo: /msvc-student/default             │
└─────────────────────────────────────────────┘
         ▲         ▲         ▲
         │         │         │
    Eureka    Student    Course
    Server    Service    Service
```

**Beneficio principal:** Si necesitas cambiar la URL de la base de datos de todos los servicios, solo cambias un archivo en el Config Server, no en cada servicio.

---

### 2. 🔍 Netflix Eureka Server

**Rol:** Service Registry — el "directorio telefónico" de los microservicios.

```
┌─────────────────────────────────────────────────────┐
│                  EUREKA SERVER                      │
│                                                     │
│  Registro de Servicios:                             │
│  ┌─────────────────────────────────────────────┐   │
│  │ Nombre          │ Host      │ Puerto │ Estado│   │
│  ├─────────────────┼───────────┼────────┼───────┤   │
│  │ MSVC-GATEWAY    │ localhost │ 8080   │  UP   │   │
│  │ MSVC-STUDENT    │ localhost │ 8090   │  UP   │   │
│  │ MSVC-COURSE     │ localhost │ 9090   │  UP   │   │
│  └─────────────────────────────────────────────┘   │
│                                                     │
│  Heartbeat cada 30s → Si falla, se marca DOWN      │
│  Dashboard: http://localhost:8761                   │
└─────────────────────────────────────────────────────┘
```

**Proceso de Registro:**

```
1. Servicio arranca
   │
   ▼
2. Lee configuración de Eureka URL
   (http://localhost:8761/eureka/)
   │
   ▼
3. Envía POST /eureka/apps/{appName}
   con su host, puerto y metadata
   │
   ▼
4. Eureka registra el servicio
   │
   ▼
5. Servicio envía heartbeat cada 30s
   │
   ▼
6. Si no hay heartbeat en 90s →
   Eureka desregistra el servicio
```

---

### 3. 🚪 Spring Cloud Gateway

**Rol:** Reverse proxy inteligente — único punto de entrada.

```
CLIENTE
  │
  │ GET http://localhost:8080/api/course/all
  ▼
┌─────────────────────────────────────────────────────┐
│                   API GATEWAY                       │
│                                                     │
│  Predicates (¿A quién va?):                         │
│  ┌─────────────────────────────────────────────┐   │
│  │ Path=/api/student/**  → uri: localhost:8090 │   │
│  │ Path=/api/course/**   → uri: localhost:9090 │   │
│  └─────────────────────────────────────────────┘   │
│                                                     │
│  Filters (¿Qué hacer con la petición?):             │
│  - Agregar headers de autenticación (futuro)        │
│  - Rate limiting (futuro)                           │
│  - Circuit breaker (futuro)                         │
│                                                     │
└─────────────────────────────────────────────────────┘
  │
  │ Redirige a http://localhost:9090/api/course/all
  ▼
COURSE SERVICE
```

---

### 4. 👨‍🎓 Student Service — Arquitectura Interna

**Capas de la aplicación (Clean Architecture simplificada):**

```
┌─────────────────────────────────────────────────────┐
│          studentController.java                     │
│                                                     │
│  Capa de Presentación (REST API)                    │
│  - Recibe peticiones HTTP                           │
│  - Valida entrada básica                            │
│  - Delega a la capa de servicio                     │
│  - Formatea respuesta HTTP                          │
└────────────────────┬────────────────────────────────┘
                     │ Usa
                     ▼
┌─────────────────────────────────────────────────────┐
│   IstudetnService  ←  studentServiceImpl.java       │
│                                                     │
│  Capa de Servicio (Lógica de Negocio)               │
│  - Implementa reglas de negocio                     │
│  - Orquesta llamadas al repositorio                 │
│  - Transacciones                                    │
└────────────────────┬────────────────────────────────┘
                     │ Usa
                     ▼
┌─────────────────────────────────────────────────────┐
│             studentRepository.java                  │
│         (extends CrudRepository)                    │
│                                                     │
│  Capa de Persistencia (Acceso a Datos)              │
│  - CRUD automático via Spring Data JPA              │
│  - Query personalizada: findByCourseId()            │
└────────────────────┬────────────────────────────────┘
                     │ Mapea
                     ▼
┌─────────────────────────────────────────────────────┐
│                student.java (Entity)                │
│                                                     │
│  @Entity @Table(name = "students")                  │
│  - id, name, lastname, email, courseId              │
│  - Mapeado a MySQL via Hibernate                    │
└─────────────────────────────────────────────────────┘
```

---

### 5. 📚 Course Service — Arquitectura Interna + Feign

```
┌─────────────────────────────────────────────────────┐
│             courseController.java                   │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│   IcourseService  ←  courseServiceImpl.java         │
│                                                     │
│  findStudentByCourse(idCourse):                     │
│    1. Busca curso en repositorio                    │
│    2. Llama studentClient.findAllStudentByCourse()  │
│    3. Combina datos → studentByCourseResponse       │
│    4. Retorna respuesta al controlador              │
└──────────┬─────────────────┬───────────────────────┘
           │                 │
           ▼                 ▼
┌──────────────────┐  ┌──────────────────────────────┐
│ IcourseRepository│  │      studentClient.java      │
│   (PostgreSQL)   │  │      (Feign Client)          │
│                  │  │                              │
│  Datos del curso │  │  Llama a msvc-student via    │
│                  │  │  HTTP a través del Gateway   │
└──────────────────┘  └──────────────────────────────┘
```

---

## Patrones Implementados

### 1. 🏛️ Pattern: API Gateway

**Problema resuelto:** Los clientes necesitarían conocer la URL de cada microservicio y manejar cambios de dirección.

**Solución:** Un único punto de entrada que abstrae la topología interna del sistema.

```
SIN Gateway:                    CON Gateway:
Cliente → :8090/api/student     Cliente → :8080/api/student
Cliente → :9090/api/course      Cliente → :8080/api/course
(El cliente conoce todos)       (El cliente conoce uno)
```

### 2. 🔍 Pattern: Service Registry & Discovery

**Problema resuelto:** En entornos dinámicos (contenedores, cloud), las IPs de los servicios cambian constantemente.

**Solución:** Los servicios se registran con su nombre lógico y se descubren por nombre.

```
SIN Discovery:          CON Discovery:
URL: "192.168.1.5:8090" URL: "msvc-student" (nombre lógico)
(IP puede cambiar)      (Eureka resuelve la IP actual)
```

### 3. ⚙️ Pattern: Externalized Configuration

**Problema resuelto:** Cada servicio tiene su propia configuración que debe mantenerse sincronizada.

**Solución:** El Config Server centraliza la configuración, los servicios la obtienen al arrancar.

### 4. 📤 Pattern: Database per Service

**Problema resuelto:** Si los servicios comparten base de datos, un cambio de esquema puede afectar a todos.

**Solución:** Cada servicio tiene su propia base de datos independiente.

```
Student Service ─── MySQL (students_db)     ← Solo Student puede escribir aquí
Course Service  ─── PostgreSQL (course_db)  ← Solo Course puede escribir aquí
```

### 5. 🔗 Pattern: Synchronous Inter-Service Communication (via Feign)

**Problema resuelto:** Course Service necesita datos del Student Service para responder la consulta de "curso con sus alumnos".

**Solución:** OpenFeign genera un cliente HTTP declarativo para llamar al otro servicio.

```java
// Así de simple es llamar a otro microservicio con Feign:
@FeignClient(name = "msvc-student", url = "localhost:8080/api/student")
public interface studentClient {
    @GetMapping("/serach_by_id_course/{idCourse}")
    List<studentDTO> findAllStudentByCourse(@PathVariable Long idCourse);
}
```

---

## Diagramas de Secuencia

### Secuencia: Crear Estudiante

```
Cliente     Gateway     Student Svc     MySQL
  │            │             │            │
  │─POST ─────►│             │            │
  │/api/student│             │            │
  │/create     │             │            │
  │            │─ Forward ──►│            │
  │            │/api/student │            │
  │            │/create      │            │
  │            │             │─ INSERT ──►│
  │            │             │            │─ OK
  │            │             │◄───────────│
  │            │             │            │
  │◄── 201 ───│◄── 201 ─────│            │
```

### Secuencia: Obtener Curso con Estudiantes

```
Cliente     Gateway   Course Svc   Student Svc   MySQL   PostgreSQL
  │            │           │            │           │         │
  │─GET ──────►│           │            │           │         │
  │/course/    │           │            │           │         │
  │search_     │           │            │           │         │
  │students/1  │           │            │           │         │
  │            │─Forward──►│            │           │         │
  │            │           │─ SELECT ───────────────────────►│
  │            │           │◄────────────────────────────────│
  │            │           │ {id:1, name:"Arq.", teacher:"..."}
  │            │           │            │           │         │
  │            │           │─ Feign ───►│           │         │
  │            │           │  GET /api/ │           │         │
  │            │           │  student/  │           │         │
  │            │           │  serach_by_│           │         │
  │            │           │  id_course/│           │         │
  │            │           │  1         │           │         │
  │            │           │            │─ SELECT──►│         │
  │            │           │            │◄──────────│         │
  │            │           │◄──────────│           │         │
  │            │           │ [{name:"Juan",...}]    │         │
  │            │           │            │           │         │
  │            │           │ Combina datos          │         │
  │            │           │            │           │         │
  │◄── 200 ───│◄── 200 ──│            │           │         │
  │ {courseName, teacher, studentDTOList: [...]}    │         │
```

---

## Modelo de Datos

### Entidad: Student (MySQL)

```sql
CREATE TABLE students (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255),
    course_id  BIGINT,
    PRIMARY KEY (id)
);
```

**Notas:**
- `course_id` es una referencia lógica al curso (no es una FK real con constraint)
- Un estudiante pertenece a un solo curso a la vez

### Entidad: Course (PostgreSQL)

```sql
CREATE TABLE courses (
    id      BIGINT       NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name    VARCHAR(255),
    teacher VARCHAR(255),
    PRIMARY KEY (id)
);
```

### Relación Conceptual

```
┌─────────────────────┐          ┌─────────────────────┐
│       courses       │          │       students      │
├─────────────────────┤          ├─────────────────────┤
│ id (PK)             │◄────┐    │ id (PK)             │
│ name                │     │    │ name                │
│ teacher             │     └────│ course_id           │
└─────────────────────┘          │ last_name           │
   PostgreSQL                    │ email               │
                                 └─────────────────────┘
                                      MySQL

  Un curso tiene muchos estudiantes (1:N)
  Un estudiante pertenece a un curso (N:1)
```

> ⚠️ **Nota de arquitectura:** Las tablas están en diferentes bases de datos (MySQL y PostgreSQL), por lo que no se puede usar una FK real entre ellas. La relación se mantiene a nivel de aplicación, no de base de datos.

---

## Puertos y Redes

| Servicio | Puerto | Protocolo | Descripción |
|---|---|---|---|
| Config Server | `8888` | HTTP | Configuración centralizada |
| Eureka Server | `8761` | HTTP | Service Registry + Dashboard |
| API Gateway | `8080` | HTTP | Punto de entrada principal |
| Student Service | `8090` | HTTP | API REST de estudiantes |
| Course Service | `9090` | HTTP | API REST de cursos |
| MySQL | `3306` | TCP | Base de datos de estudiantes |
| PostgreSQL | `5432` | TCP | Base de datos de cursos |

### Mapa de Conectividad

```
             ┌──────────┐
             │  Cliente │
             └────┬─────┘
                  │ :8080
                  ▼
             ┌──────────┐
             │ Gateway  │◄──────────────────────────┐
             └────┬─────┘                           │
        ┌─────────┤─────────┐                       │
        │:8090    │         │:9090                  │
        ▼         │         ▼                       │
   ┌─────────┐    │   ┌─────────┐    ───────────    │
   │ Student │    │   │ Course  │─────:8080/api/─────┘
   │ Service │    │   │ Service │   student/** (Feign)
   └────┬────┘    │   └────┬────┘
        │:3306    │        │:5432
        ▼         │        ▼
   ┌─────────┐    │   ┌─────────┐
   │  MySQL  │    │   │Postgres │
   └─────────┘    │   └─────────┘
                  │
        Todos     │     usan
                  ▼
          ┌───────────────┐    ┌───────────────┐
          │ Eureka :8761  │    │ Config :8888  │
          └───────────────┘    └───────────────┘
```

---

## Consideraciones de Diseño

### ✅ Decisiones Correctas

1. **Eureka para Service Discovery:** Permite escalar servicios sin cambiar configuraciones de red
2. **API Gateway como único punto de entrada:** Simplifica el cliente y permite agregar cross-cutting concerns (auth, logging) en un solo lugar
3. **Config Server:** Facilita el cambio de configuración sin redeployments
4. **OpenFeign para inter-service calls:** Código declarativo y legible para llamadas HTTP
5. **Bases de datos independientes:** Desacoplamiento real entre servicios

### ⚠️ Áreas de Mejora (para Producción)

1. **Circuit Breaker:** Agregar Resilience4J para manejar fallos del Student Service cuando Course le llama
2. **Feign con Service Discovery:** Cambiar URL hardcodeada por nombre de servicio Eureka
3. **Async Communication:** Para operaciones no críticas, usar mensajería (RabbitMQ, Kafka) en lugar de llamadas síncronas
4. **Distributed Tracing:** Agregar Sleuth + Zipkin para rastrear peticiones a través de servicios
5. **Centralized Logging:** ELK Stack o similar para logs unificados
6. **Secretos:** Usar Vault o variables de entorno para credenciales

### 🚀 Roadmap de Mejoras

```
Fase 1 (Actual) ─── Arquitectura básica funcional
                    Config Server, Eureka, Gateway, 2 servicios

Fase 2 ─────────── Resiliencia y Observabilidad
                    Circuit Breaker, Distributed Tracing, Logging

Fase 3 ─────────── Contenedorización
                    Docker, Docker Compose

Fase 4 ─────────── Seguridad
                    Spring Security, JWT, OAuth2

Fase 5 ─────────── Producción
                    Kubernetes, CI/CD, Monitoring dashboards
```
