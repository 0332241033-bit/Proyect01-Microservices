# 🤝 Guía de Contribución — Proyect01 Microservices

> ¡Gracias por tu interés en contribuir a este proyecto! Este documento explica cómo participar de manera efectiva.

---

## 📋 Tabla de Contenidos

- [Código de Conducta](#código-de-conducta)
- [¿Cómo Puedo Contribuir?](#cómo-puedo-contribuir)
- [Configuración del Entorno de Desarrollo](#configuración-del-entorno-de-desarrollo)
- [Flujo de Trabajo con Git](#flujo-de-trabajo-con-git)
- [Estándares de Código](#estándares-de-código)
- [Convenciones de Commit](#convenciones-de-commit)
- [Pull Requests](#pull-requests)
- [Reportar Bugs](#reportar-bugs)
- [Sugerir Mejoras](#sugerir-mejoras)

---

## Código de Conducta

Este proyecto sigue los principios de respeto mutuo y colaboración constructiva. Se espera que todos los contribuidores:

- **Sean respetuosos** en sus comentarios y discusiones
- **Acepten feedback constructivo** de manera positiva
- **Ayuden a otros** especialmente a principiantes
- **Se enfoquen en el aprendizaje** — este es un proyecto educativo

---

## ¿Cómo Puedo Contribuir?

### 🐛 Reportar Bugs

¿Encontraste un error? Abre un [issue en GitHub](https://github.com/0332241033-bit/Proyect01-Microservices/issues) con:

- Descripción clara del problema
- Pasos para reproducirlo
- Comportamiento esperado vs comportamiento actual
- Capturas de pantalla o logs relevantes
- Versión de Java, Maven, MySQL y PostgreSQL que usas

### 💡 Sugerir Mejoras

¿Tienes una idea para mejorar el proyecto? Abre un issue con la etiqueta `enhancement` describiendo:

- El problema que resuelve
- Cómo funcionaría
- Alternativas consideradas

### 📝 Mejorar Documentación

La documentación siempre puede mejorar. Puedes:

- Corregir errores tipográficos
- Agregar ejemplos más claros
- Traducir documentación
- Agregar diagramas o imágenes

### 🔧 Contribuir Código

Áreas donde se aceptan contribuciones:

| Área | Descripción | Dificultad |
|---|---|---|
| **Tests** | Agregar pruebas unitarias e integración | 🟡 Media |
| **Docker** | Agregar Dockerfiles y docker-compose | 🟡 Media |
| **Circuit Breaker** | Implementar Resilience4J | 🟠 Alta |
| **Security** | Agregar Spring Security + JWT | 🟠 Alta |
| **Documentación** | Mejorar READMEs y comentarios | 🟢 Baja |
| **Bug fixes** | Corregir errores existentes | Variable |

---

## Configuración del Entorno de Desarrollo

### 1. Fork y Clone

```bash
# 1. Hacer fork del repositorio en GitHub (botón "Fork")

# 2. Clonar tu fork
git clone https://github.com/TU_USUARIO/Proyect01-Microservices.git
cd Proyect01-Microservices

# 3. Agregar el repositorio original como upstream
git remote add upstream https://github.com/0332241033-bit/Proyect01-Microservices.git

# Verificar remotos
git remote -v
# origin    https://github.com/TU_USUARIO/Proyect01-Microservices.git
# upstream  https://github.com/0332241033-bit/Proyect01-Microservices.git
```

### 2. Mantener tu Fork Actualizado

```bash
# Obtener cambios del repositorio original
git fetch upstream

# Actualizar tu rama main
git checkout main
git merge upstream/main
```

### 3. Seguir la Guía de Setup

Consulta la [Guía de Setup](setup-guide.md) para configurar el entorno de desarrollo completo.

---

## Flujo de Trabajo con Git

### Crear una Rama para tu Contribución

```bash
# Asegúrate de estar en main y actualizado
git checkout main
git pull upstream main

# Crear nueva rama (usar convención de nombres)
git checkout -b tipo/descripcion-corta

# Ejemplos:
git checkout -b feature/agregar-docker-compose
git checkout -b fix/typo-endpoint-search
git checkout -b docs/mejorar-readme-student
git checkout -b test/agregar-tests-student-service
```

### Tipos de Rama

| Prefijo | Usar para |
|---|---|
| `feature/` | Nueva funcionalidad |
| `fix/` | Corrección de bugs |
| `docs/` | Cambios en documentación |
| `test/` | Agregar o corregir tests |
| `refactor/` | Refactorización de código |
| `chore/` | Tareas de mantenimiento |

### Hacer Commits

```bash
# Ver estado de cambios
git status

# Agregar cambios específicos
git add microservices.student/src/...

# O agregar todos los cambios
git add .

# Hacer commit (ver convenciones abajo)
git commit -m "feat(student): agregar endpoint de búsqueda por email"

# Subir a tu fork
git push origin feature/mi-nueva-feature
```

---

## Estándares de Código

### Java — Convenciones del Proyecto

#### Nomenclatura

```java
// ✅ Clases: PascalCase
public class StudentController {}
public class CourseServiceImpl {}

// ✅ Métodos y variables: camelCase
public List<Student> findAllStudents() {}
private Long courseId;

// ✅ Constantes: UPPER_SNAKE_CASE
private static final String DEFAULT_ROLE = "STUDENT";

// ✅ Paquetes: lowercase con punto
package com.microservice.student.controller;
```

#### Estructura de Clases

```java
// Orden recomendado en las clases:
// 1. Constantes
// 2. Campos/Atributos
// 3. Constructores
// 4. Getters/Setters (o usar Lombok)
// 5. Métodos de negocio
// 6. Métodos de utilidad/privados
```

#### Spring Boot Best Practices

```java
// ✅ Usar @Service para la capa de servicio
@Service
public class StudentServiceImpl implements IStudentService {}

// ✅ Inyección de dependencias por constructor (mejor que @Autowired en campo)
private final IStudentRepository studentRepository;

public StudentServiceImpl(IStudentRepository studentRepository) {
    this.studentRepository = studentRepository;
}

// ✅ Interfaces para servicios
public interface IStudentService {
    List<Student> findAllStudents();
}

// ✅ ResponseEntity para respuestas HTTP explícitas
@GetMapping("/all")
public ResponseEntity<List<Student>> findAll() {
    List<Student> students = studentService.findAllStudents();
    return students.isEmpty() 
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(students);
}
```

#### Buenas Prácticas con JPA

```java
// ✅ Siempre definir @Table con nombre explícito
@Entity
@Table(name = "students")
public class Student {}

// ✅ Usar @Column para nombres con convención
@Column(name = "last_name")
private String lastname;

// ✅ DDL-auto: usar 'update' (no 'create') para no perder datos
spring.jpa.hibernate.ddl-auto=update
```

### Formato de Código

- **Indentación:** 4 espacios (no tabs)
- **Longitud máxima de línea:** 120 caracteres
- **Llaves:** En la misma línea (`K&R style`)
- **Imports:** Sin wildcards (`import java.util.List` no `import java.util.*`)

---

## Convenciones de Commit

Este proyecto sigue **Conventional Commits** para tener un historial claro y generar changelogs automáticos.

### Formato

```
<tipo>(<scope>): <descripción corta>

[cuerpo opcional]

[footer opcional]
```

### Tipos de Commit

| Tipo | Descripción | Ejemplo |
|---|---|---|
| `feat` | Nueva funcionalidad | `feat(student): agregar búsqueda por email` |
| `fix` | Corrección de bug | `fix(gateway): corregir typo en ruta de cursos` |
| `docs` | Documentación | `docs(readme): agregar sección de contribución` |
| `style` | Formato, sin cambios de lógica | `style(course): aplicar formato de código` |
| `refactor` | Refactorización | `refactor(student): extraer lógica de validación` |
| `test` | Agregar o corregir tests | `test(course): agregar test de integración` |
| `chore` | Mantenimiento, build, CI | `chore(deps): actualizar versión de Spring Boot` |
| `perf` | Mejora de rendimiento | `perf(student): optimizar query de búsqueda` |

### Scopes Disponibles

| Scope | Servicio |
|---|---|
| `student` | microservices.student |
| `course` | microservices.course |
| `gateway` | microservices.gateway |
| `eureka` | microservices.eureka |
| `config` | microservices.config |
| `docs` | Documentación |
| `deps` | Dependencias |
| `ci` | CI/CD |

### Ejemplos

```bash
# ✅ Buenos commits
git commit -m "feat(student): agregar endpoint de actualización de email"
git commit -m "fix(course): corregir NullPointerException cuando lista de estudiantes es vacía"
git commit -m "docs(api-reference): agregar ejemplos de respuesta para todos los endpoints"
git commit -m "test(student): agregar tests unitarios para StudentServiceImpl"
git commit -m "refactor(course): usar constructor injection en lugar de @Autowired"

# ❌ Malos commits
git commit -m "fix"
git commit -m "cambios"
git commit -m "WIP"
git commit -m "arregle el bug"
```

---

## Pull Requests

### Antes de Abrir un PR

- [ ] El código compila sin errores (`mvn clean install`)
- [ ] Los tests existentes pasan (si los hay)
- [ ] El código sigue las convenciones del proyecto
- [ ] La documentación fue actualizada si es necesario
- [ ] Los commits siguen las convenciones de Conventional Commits

### Estructura del PR

Al abrir un Pull Request, incluye:

**Título:** Usa el mismo formato que los commits: `feat(scope): descripción`

**Descripción:**

```markdown
## ¿Qué hace este PR?
Descripción clara de los cambios realizados.

## ¿Por qué es necesario?
Explica el problema que resuelve o la mejora que aporta.

## ¿Cómo probarlo?
Pasos para verificar los cambios:
1. Iniciar los servicios
2. Ejecutar: `curl http://localhost:8080/api/...`
3. Verificar que la respuesta es: `{...}`

## Capturas de pantalla (si aplica)
[Agregar imágenes aquí]

## Checklist
- [ ] Código compilable
- [ ] Tests pasan
- [ ] Documentación actualizada
- [ ] Convenciones de código seguidas
```

### Proceso de Review

1. **Abre el PR** contra la rama `main` del repositorio original
2. **Espera el review** — el mantenedor revisará y puede pedir cambios
3. **Responde los comentarios** — atiende el feedback constructivamente
4. **Actualiza si es necesario** — haz push de los cambios solicitados
5. **Merge** — cuando sea aprobado, se integrará a `main`

---

## Reportar Bugs

### Template para Issues de Bug

```markdown
**Descripción del Bug**
Descripción clara y concisa del bug.

**Pasos para Reproducir**
1. Iniciar servicios en el orden correcto
2. Enviar petición: `curl -X POST http://localhost:8080/api/...`
3. Ver error en la respuesta

**Comportamiento Esperado**
¿Qué debería pasar?

**Comportamiento Actual**
¿Qué pasa en realidad?

**Screenshots / Logs**
```
[pegar logs relevantes aquí]
```

**Entorno**
- OS: [ej. Ubuntu 22.04, Windows 11, macOS 14]
- Java: [ej. OpenJDK 17.0.8]
- MySQL: [ej. 8.0.34]
- PostgreSQL: [ej. 15.4]

**Contexto Adicional**
Cualquier otra información relevante.
```

---

## Sugerir Mejoras

### Template para Issues de Mejora

```markdown
**¿Está relacionada con un problema tu sugerencia?**
Descripción clara del problema. Ej: "Me frustra cuando..."

**Describe la solución que te gustaría**
Descripción clara de lo que te gustaría que pasara.

**Describe las alternativas consideradas**
Otras soluciones o features que consideraste.

**Contexto adicional**
Cualquier otro contexto, screenshots o ejemplos sobre la feature request.
```

---

## 🙏 Agradecimientos

¡Gracias a todos los que contribuyen a este proyecto! Cada contribution, sin importar cuán pequeña, ayuda a mejorar el proyecto y a que otros aprendan microservicios con Spring Cloud.

---

<div align="center">

¿Preguntas? Abre un [issue](https://github.com/0332241033-bit/Proyect01-Microservices/issues) o inicia una [discusión](https://github.com/0332241033-bit/Proyect01-Microservices/discussions).

</div>
