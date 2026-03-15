# 📡 API Reference — Proyect01 Microservices

> Documentación completa de todos los endpoints disponibles en el sistema.
>
> **Base URL:** `http://localhost:8080` (API Gateway — único punto de entrada)

---

## 📋 Tabla de Contenidos

- [Convenciones](#convenciones)
- [Códigos de Respuesta HTTP](#códigos-de-respuesta-http)
- [👨‍🎓 Student Service API](#-student-service-api)
  - [Crear Estudiante](#post-apistudentcreate)
  - [Listar Todos los Estudiantes](#get-apistudentall)
  - [Buscar por ID](#get-apistudentsearchid)
  - [Buscar por Curso](#get-apistudentserach_by_id_courseidcourse)
- [📚 Course Service API](#-course-service-api)
  - [Crear Curso](#post-apicoursecreate)
  - [Listar Todos los Cursos](#get-apicourseall)
  - [Buscar por ID](#get-apicoursesearchid)
  - [Buscar Curso con Estudiantes](#get-apicoursesearch_studentsidcourse)
- [🩺 Health Endpoints](#-health-endpoints)
- [Ejemplos con cURL](#ejemplos-con-curl)
- [Colección Postman](#colección-postman)

---

## Convenciones

| Símbolo | Significado |
|---|---|
| `{id}` | Parámetro de path variable — reemplazar con valor real |
| `*` | Campo requerido |
| `?` | Campo opcional |

**Headers comunes:**
```http
Content-Type: application/json
Accept: application/json
```

---

## Códigos de Respuesta HTTP

| Código | Nombre | Cuándo se usa |
|---|---|---|
| `200 OK` | Éxito | Solicitud procesada correctamente con datos |
| `201 Created` | Creado | Recurso creado exitosamente |
| `204 No Content` | Sin contenido | Solicitud exitosa pero sin datos que retornar |
| `404 Not Found` | No encontrado | El recurso solicitado no existe |
| `500 Internal Server Error` | Error del servidor | Error inesperado en el servidor |

---

## 👨‍🎓 Student Service API

**Prefix:** `/api/student`  
**Puerto directo:** `8090` (también accesible via Gateway en `:8080`)

---

### POST `/api/student/create`

Crea un nuevo estudiante en el sistema.

**Request:**

```http
POST http://localhost:8080/api/student/create
Content-Type: application/json
```

**Body:**

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| `name` | `String` | ✅ | Nombre del estudiante |
| `lastname` | `String` | ✅ | Apellido del estudiante |
| `email` | `String` | ✅ | Correo electrónico |
| `courseId` | `Long` | ✅ | ID del curso al que pertenece |

**Ejemplo de cuerpo:**

```json
{
  "name": "Juan",
  "lastname": "Pérez García",
  "email": "juan.perez@university.edu",
  "courseId": 1
}
```

**Respuestas:**

| Código | Descripción | Cuerpo |
|---|---|---|
| `201 Created` | Estudiante creado | (vacío) |

**Ejemplo completo con cURL:**

```bash
curl -X POST http://localhost:8080/api/student/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan",
    "lastname": "Pérez García",
    "email": "juan.perez@university.edu",
    "courseId": 1
  }'
```

---

### GET `/api/student/all`

Retorna la lista completa de todos los estudiantes registrados.

**Request:**

```http
GET http://localhost:8080/api/student/all
```

**Respuestas:**

| Código | Descripción | Cuerpo |
|---|---|---|
| `200 OK` | Lista de estudiantes | Array de objetos `Student` |
| `204 No Content` | No hay estudiantes | (vacío) |

**Ejemplo de respuesta `200 OK`:**

```json
[
  {
    "id": 1,
    "name": "Juan",
    "lastname": "Pérez García",
    "email": "juan.perez@university.edu",
    "courseId": 1
  },
  {
    "id": 2,
    "name": "María",
    "lastname": "González Ruiz",
    "email": "maria.gonzalez@university.edu",
    "courseId": 1
  },
  {
    "id": 3,
    "name": "Carlos",
    "lastname": "Martínez López",
    "email": "carlos.martinez@university.edu",
    "courseId": 2
  }
]
```

**Ejemplo con cURL:**

```bash
curl http://localhost:8080/api/student/all
```

---

### GET `/api/student/search/{id}`

Busca y retorna un estudiante por su ID único.

**Request:**

```http
GET http://localhost:8080/api/student/search/{id}
```

**Path Parameters:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | `Long` | ID único del estudiante |

**Respuestas:**

| Código | Descripción | Cuerpo |
|---|---|---|
| `200 OK` | Estudiante encontrado | Objeto `Student` |
| `404 Not Found` | No existe el ID | (vacío) |

**Ejemplo de respuesta `200 OK`:**

```json
{
  "id": 1,
  "name": "Juan",
  "lastname": "Pérez García",
  "email": "juan.perez@university.edu",
  "courseId": 1
}
```

**Ejemplos con cURL:**

```bash
# Buscar estudiante con ID 1
curl http://localhost:8080/api/student/search/1

# Buscar estudiante con ID que no existe
curl -v http://localhost:8080/api/student/search/999
# Respuesta: HTTP/1.1 404 Not Found
```

---

### GET `/api/student/serach_by_id_course/{idCourse}`

Retorna todos los estudiantes inscritos en un curso específico.

> ⚠️ **Nota:** El endpoint tiene una errata tipográfica en su nombre (`serach` en lugar de `search`). Se conserva así para mantener compatibilidad.

**Request:**

```http
GET http://localhost:8080/api/student/serach_by_id_course/{idCourse}
```

**Path Parameters:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `idCourse` | `Long` | ID del curso |

**Respuestas:**

| Código | Descripción | Cuerpo |
|---|---|---|
| `200 OK` | Lista de estudiantes del curso | Array de objetos `Student` |

**Ejemplo de respuesta `200 OK`:**

```json
[
  {
    "id": 1,
    "name": "Juan",
    "lastname": "Pérez García",
    "email": "juan.perez@university.edu",
    "courseId": 1
  },
  {
    "id": 2,
    "name": "María",
    "lastname": "González Ruiz",
    "email": "maria.gonzalez@university.edu",
    "courseId": 1
  }
]
```

**Ejemplo con cURL:**

```bash
# Todos los estudiantes del curso 1
curl http://localhost:8080/api/student/serach_by_id_course/1
```

---

## 📚 Course Service API

**Prefix:** `/api/course`  
**Puerto directo:** `9090` (también accesible via Gateway en `:8080`)

---

### POST `/api/course/create`

Crea un nuevo curso en el sistema.

**Request:**

```http
POST http://localhost:8080/api/course/create
Content-Type: application/json
```

**Body:**

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| `name` | `String` | ✅ | Nombre del curso |
| `teacher` | `String` | ✅ | Nombre del docente |

**Ejemplo de cuerpo:**

```json
{
  "name": "Arquitectura de Software",
  "teacher": "Dr. García López"
}
```

**Respuestas:**

| Código | Descripción | Cuerpo |
|---|---|---|
| `201 Created` | Curso creado | (vacío) |

**Ejemplo con cURL:**

```bash
curl -X POST http://localhost:8080/api/course/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Arquitectura de Software",
    "teacher": "Dr. García López"
  }'
```

---

### GET `/api/course/all`

Retorna la lista completa de todos los cursos registrados.

**Request:**

```http
GET http://localhost:8080/api/course/all
```

**Respuestas:**

| Código | Descripción | Cuerpo |
|---|---|---|
| `200 OK` | Lista de cursos | Array de objetos `Course` |
| `204 No Content` | No hay cursos | (vacío) |

**Ejemplo de respuesta `200 OK`:**

```json
[
  {
    "id": 1,
    "name": "Arquitectura de Software",
    "teacher": "Dr. García López"
  },
  {
    "id": 2,
    "name": "Bases de Datos Avanzadas",
    "teacher": "Ing. Martínez"
  },
  {
    "id": 3,
    "name": "Desarrollo Web Full Stack",
    "teacher": "Lic. Rodríguez"
  }
]
```

**Ejemplo con cURL:**

```bash
curl http://localhost:8080/api/course/all
```

---

### GET `/api/course/search/{id}`

Busca y retorna un curso por su ID único.

**Request:**

```http
GET http://localhost:8080/api/course/search/{id}
```

**Path Parameters:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | `Long` | ID único del curso |

**Respuestas:**

| Código | Descripción | Cuerpo |
|---|---|---|
| `200 OK` | Curso encontrado | Objeto `Course` |
| `404 Not Found` | No existe el ID | (vacío) |

**Ejemplo de respuesta `200 OK`:**

```json
{
  "id": 1,
  "name": "Arquitectura de Software",
  "teacher": "Dr. García López"
}
```

**Ejemplo con cURL:**

```bash
curl http://localhost:8080/api/course/search/1
```

---

### GET `/api/course/search_students/{idCourse}`

⭐ **Endpoint más complejo del sistema.** Retorna un curso con la lista completa de sus estudiantes inscritos. Realiza una llamada interna al Student Service.

**Request:**

```http
GET http://localhost:8080/api/course/search_students/{idCourse}
```

**Path Parameters:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `idCourse` | `Long` | ID del curso |

**Respuestas:**

| Código | Descripción | Cuerpo |
|---|---|---|
| `200 OK` | Curso con estudiantes | Objeto `studentByCourseResponse` |

**Estructura de la respuesta:**

```json
{
  "courseName": "String — nombre del curso",
  "teacher": "String — nombre del docente",
  "studentDTOList": [
    {
      "name": "String — nombre del estudiante",
      "lastname": "String — apellido",
      "email": "String — correo electrónico",
      "courseId": "Long — ID del curso"
    }
  ]
}
```

**Ejemplo de respuesta `200 OK`:**

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
      "lastname": "González Ruiz",
      "email": "maria.gonzalez@university.edu",
      "courseId": 1
    },
    {
      "name": "Carlos",
      "lastname": "Martínez López",
      "email": "carlos.martinez@university.edu",
      "courseId": 1
    }
  ]
}
```

**Ejemplo con cURL:**

```bash
curl http://localhost:8080/api/course/search_students/1
```

**¿Qué ocurre internamente?**

1. Gateway recibe la petición y la redirige al Course Service (`:9090`)
2. Course Service consulta PostgreSQL para obtener los datos del curso
3. Course Service llama vía Feign al Student Service para obtener los alumnos del curso
4. Course Service combina ambas respuestas y las retorna al cliente

---

## 🩺 Health Endpoints

Spring Boot Actuator expone endpoints de salud en cada servicio. Útiles para monitoreo y liveness/readiness probes.

| Servicio | URL de Health | Acceso via Gateway |
|---|---|---|
| Config Server | `http://localhost:8888/actuator/health` | No aplica |
| Eureka Server | `http://localhost:8761/actuator/health` | No aplica |
| API Gateway | `http://localhost:8080/actuator/health` | Directo |
| Student Service | `http://localhost:8090/actuator/health` | No enrutado |
| Course Service | `http://localhost:9090/actuator/health` | No enrutado |

**Respuesta de `/actuator/health`:**

```json
{
  "status": "UP"
}
```

**Verificar todos los servicios con bash:**

```bash
for port in 8888 8761 8080 8090 9090; do
  echo -n "Puerto $port: "
  curl -s http://localhost:$port/actuator/health | grep -o '"status":"[^"]*"'
done
```

---

## Ejemplos con cURL

### Flujo Completo de Prueba

Ejecuta estos comandos en orden para probar todo el sistema:

```bash
# 1. Verificar que todos los servicios están activos
echo "=== Health Checks ==="
curl -s http://localhost:8888/actuator/health
curl -s http://localhost:8761/actuator/health
curl -s http://localhost:8080/actuator/health

# 2. Crear dos cursos
echo "=== Crear Cursos ==="
curl -X POST http://localhost:8080/api/course/create \
  -H "Content-Type: application/json" \
  -d '{"name": "Arquitectura de Software", "teacher": "Dr. García"}'

curl -X POST http://localhost:8080/api/course/create \
  -H "Content-Type: application/json" \
  -d '{"name": "Bases de Datos", "teacher": "Ing. Martínez"}'

# 3. Listar cursos creados
echo "=== Listar Cursos ==="
curl http://localhost:8080/api/course/all

# 4. Crear estudiantes en el curso 1
echo "=== Crear Estudiantes ==="
curl -X POST http://localhost:8080/api/student/create \
  -H "Content-Type: application/json" \
  -d '{"name": "Juan", "lastname": "Pérez", "email": "juan@uni.edu", "courseId": 1}'

curl -X POST http://localhost:8080/api/student/create \
  -H "Content-Type: application/json" \
  -d '{"name": "María", "lastname": "González", "email": "maria@uni.edu", "courseId": 1}'

curl -X POST http://localhost:8080/api/student/create \
  -H "Content-Type: application/json" \
  -d '{"name": "Carlos", "lastname": "López", "email": "carlos@uni.edu", "courseId": 2}'

# 5. Listar todos los estudiantes
echo "=== Listar Estudiantes ==="
curl http://localhost:8080/api/student/all

# 6. Buscar curso con sus estudiantes (llamada inter-servicio)
echo "=== Curso con Estudiantes ==="
curl http://localhost:8080/api/course/search_students/1

# 7. Buscar estudiantes del curso 2
echo "=== Estudiantes del Curso 2 ==="
curl http://localhost:8080/api/student/serach_by_id_course/2
```

---

## Colección Postman

Importa la siguiente colección en Postman para probar todos los endpoints:

```json
{
  "info": {
    "name": "Proyect01 Microservices API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8080"
    }
  ],
  "item": [
    {
      "name": "Students",
      "item": [
        {
          "name": "Create Student",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/api/student/create",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"name\": \"Juan\",\n  \"lastname\": \"Pérez\",\n  \"email\": \"juan@uni.edu\",\n  \"courseId\": 1\n}"
            }
          }
        },
        {
          "name": "Get All Students",
          "request": {
            "method": "GET",
            "url": "{{base_url}}/api/student/all"
          }
        },
        {
          "name": "Get Student by ID",
          "request": {
            "method": "GET",
            "url": "{{base_url}}/api/student/search/1"
          }
        },
        {
          "name": "Get Students by Course",
          "request": {
            "method": "GET",
            "url": "{{base_url}}/api/student/serach_by_id_course/1"
          }
        }
      ]
    },
    {
      "name": "Courses",
      "item": [
        {
          "name": "Create Course",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/api/course/create",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"name\": \"Arquitectura de Software\",\n  \"teacher\": \"Dr. García\"\n}"
            }
          }
        },
        {
          "name": "Get All Courses",
          "request": {
            "method": "GET",
            "url": "{{base_url}}/api/course/all"
          }
        },
        {
          "name": "Get Course by ID",
          "request": {
            "method": "GET",
            "url": "{{base_url}}/api/course/search/1"
          }
        },
        {
          "name": "Get Course with Students",
          "request": {
            "method": "GET",
            "url": "{{base_url}}/api/course/search_students/1"
          }
        }
      ]
    }
  ]
}
```

**Para importar en Postman:**
1. Abre Postman
2. Clic en **Import** (botón superior izquierdo)
3. Pega el JSON anterior o guárdalo como `postman_collection.json` e impórtalo
4. La variable `base_url` apunta a `http://localhost:8080` por defecto

---

## Notas Técnicas

### DTOs y Modelos de Respuesta

#### Student Entity

```java
{
  "id": Long,           // Auto-generado por la DB
  "name": String,       // Nombre
  "lastname": String,   // Apellido (columna: last_name)
  "email": String,      // Email
  "courseId": Long      // Referencia al curso (columna: course_id)
}
```

#### Course Entity

```java
{
  "id": Long,           // Auto-generado por la DB
  "name": String,       // Nombre del curso
  "teacher": String     // Nombre del docente
}
```

#### StudentDTO (usado en respuesta de cursos)

```java
{
  "name": String,       // Nombre del estudiante
  "lastname": String,   // Apellido
  "email": String,      // Email
  "courseId": Long      // ID del curso
}
```

#### studentByCourseResponse (respuesta de /search_students)

```java
{
  "courseName": String,              // Nombre del curso
  "teacher": String,                 // Docente
  "studentDTOList": [StudentDTO]     // Lista de estudiantes
}
```
