# 🚀 Guía de Setup — Proyect01 Microservices

> Guía paso a paso para configurar y ejecutar el proyecto en tu máquina local.

---

## 📋 Tabla de Contenidos

- [Prerrequisitos del Sistema](#prerrequisitos-del-sistema)
- [Instalación de Dependencias](#instalación-de-dependencias)
  - [Java 17](#java-17)
  - [Maven](#maven)
  - [MySQL](#mysql)
  - [PostgreSQL](#postgresql)
- [Configuración de Bases de Datos](#configuración-de-bases-de-datos)
- [Configuración del Proyecto](#configuración-del-proyecto)
- [Compilación](#compilación)
- [Ejecución de Servicios](#ejecución-de-servicios)
- [Verificación del Sistema](#verificación-del-sistema)
- [Prueba del Sistema](#prueba-del-sistema)
- [Solución de Problemas Comunes](#solución-de-problemas-comunes)

---

## Prerrequisitos del Sistema

### Requisitos Mínimos

| Componente | Versión Mínima | Recomendada |
|---|---|---|
| **Java JDK** | 17 | 17 LTS |
| **Apache Maven** | 3.8 | 3.9+ |
| **MySQL** | 8.0 | 8.0+ |
| **PostgreSQL** | 12 | 15+ |
| **RAM** | 4 GB | 8 GB |
| **Disco** | 2 GB libres | 5 GB |

### Verificar Instalaciones Existentes

```bash
# Verificar Java
java -version
# Esperado: openjdk version "17.x.x" o similar

# Verificar Maven (opcional si usas mvnw)
mvn -version
# Esperado: Apache Maven 3.x.x

# Verificar MySQL
mysql --version
# Esperado: mysql  Ver 8.x...

# Verificar PostgreSQL
psql --version
# Esperado: psql (PostgreSQL) 1x.x
```

---

## Instalación de Dependencias

### Java 17

#### 🐧 Ubuntu / Debian

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk

# Verificar
java -version

# Si tienes múltiples versiones de Java, seleccionar la 17
sudo update-alternatives --config java
```

#### 🎩 Fedora / RHEL / CentOS

```bash
sudo dnf install java-17-openjdk-devel

# Verificar
java -version
```

#### 🍎 macOS (usando Homebrew)

```bash
brew install openjdk@17

# Agregar al PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verificar
java -version
```

#### 🪟 Windows

1. Descargar el instalador de [Adoptium](https://adoptium.net/) (OpenJDK 17)
2. Ejecutar el instalador MSI
3. Marcar "Set JAVA_HOME variable" durante la instalación
4. Abrir una nueva terminal y verificar: `java -version`

---

### Maven

> 💡 **Tip:** El proyecto incluye Maven Wrapper (`./mvnw`). Si no tienes Maven instalado globalmente, puedes usar `./mvnw` en lugar de `mvn` en todos los comandos.

#### 🐧 Ubuntu / Debian

```bash
sudo apt install -y maven

# Verificar
mvn -version
```

#### 🍎 macOS

```bash
brew install maven
```

#### 🪟 Windows

1. Descargar desde [Apache Maven Downloads](https://maven.apache.org/download.cgi)
2. Extraer y agregar `/bin` al PATH de sistema
3. Verificar: `mvn -version`

---

### MySQL

#### 🐧 Ubuntu / Debian

```bash
sudo apt update
sudo apt install -y mysql-server

# Iniciar servicio
sudo systemctl start mysql
sudo systemctl enable mysql

# Configurar contraseña root (primera vez)
sudo mysql_secure_installation

# Verificar que está corriendo
sudo systemctl status mysql
```

#### 🍎 macOS

```bash
brew install mysql

# Iniciar servicio
brew services start mysql

# Configurar contraseña (primera vez)
mysql_secure_installation
```

#### 🪟 Windows

1. Descargar [MySQL Installer](https://dev.mysql.com/downloads/installer/)
2. Ejecutar el instalador
3. Seleccionar "MySQL Server" en los productos
4. Anotar la contraseña que se configura para `root`
5. El servicio se inicia automáticamente

---

### PostgreSQL

#### 🐧 Ubuntu / Debian

```bash
sudo apt update
sudo apt install -y postgresql postgresql-contrib

# Iniciar servicio
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Verificar estado
sudo systemctl status postgresql
```

#### 🍎 macOS

```bash
brew install postgresql@15

# Iniciar servicio
brew services start postgresql@15

# Agregar al PATH
echo 'export PATH="/opt/homebrew/opt/postgresql@15/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

#### 🪟 Windows

1. Descargar desde [PostgreSQL Downloads](https://www.postgresql.org/download/windows/)
2. Ejecutar el instalador
3. Anotar la contraseña del usuario `postgres`
4. El servicio se inicia automáticamente

---

## Configuración de Bases de Datos

### MySQL — Base de datos de Estudiantes

#### Paso 1: Conectarse a MySQL

```bash
# Linux/Mac
mysql -u root -p

# Windows (si MySQL está en el PATH)
mysql -u root -p
```

#### Paso 2: Crear la base de datos

```sql
-- Crear la base de datos
CREATE DATABASE students_db 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

-- Verificar creación
SHOW DATABASES;
-- Debes ver 'students_db' en la lista

-- Salir de MySQL
EXIT;
```

#### Paso 3: Verificar acceso

```bash
mysql -u root -p students_db
# Ingresa tu contraseña
# Si ves "mysql>" significa que el acceso es correcto
EXIT;
```

---

### PostgreSQL — Base de datos de Cursos

#### Paso 1: Conectarse como superusuario

```bash
# Linux
sudo -u postgres psql

# macOS
psql postgres

# Windows (buscar "psql" en el menú inicio o usar pgAdmin)
psql -U postgres
```

#### Paso 2: Crear la base de datos

```sql
-- Crear la base de datos
CREATE DATABASE course_db;

-- Verificar creación
\l
-- Debes ver 'course_db' en la lista

-- Salir de PostgreSQL
\q
```

#### Paso 3: Verificar acceso

```bash
psql -U postgres -d course_db
# Si ves "course_db=#" significa que el acceso es correcto
\q
```

---

## Configuración del Proyecto

### Clonar el Repositorio

```bash
git clone https://github.com/0332241033-bit/Proyect01-Microservices.git
cd Proyect01-Microservices
```

### Actualizar Configuración de Student Service

Edita el archivo `microservices.student/src/main/resources/application.yml`:

```yaml
server:
  port: 8090

spring:
  application:
    name: msvc-student
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/students_db
    username: root              # ← Cambiar por tu usuario MySQL
    password: TU_PASSWORD_AQUI  # ← Cambiar por tu contraseña MySQL
  jpa:
    hibernate:
      ddl-auto: create
    database: mysql
    database_platform: org.hibernate.dialect.MySQL8Dialect
  config:
    import: optional:configserver:http://localhost:8888

eureka:
  instance:
    hostname: localhost
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Actualizar Configuración de Course Service

Edita el archivo `microservices.course/src/main/resources/application.yml`:

```yaml
server:
  port: 9090

spring:
  application:
    name: msvc-course
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/course_db
    username: postgres          # ← Cambiar si es necesario
    password: TU_PASSWORD_AQUI  # ← Cambiar por tu contraseña PostgreSQL
  jpa:
    hibernate:
      ddl-auto: create
    database: postgresql
    database_platform: org.hibernate.dialect.PostgreSQLDialect
  config:
    import: optional:configserver:http://localhost:8888

eureka:
  instance:
    hostname: localhost
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

---

## Compilación

Desde la raíz del proyecto, compila todos los módulos:

```bash
# Compilar y empaquetar todo el proyecto
mvn clean install -DskipTests

# O usando Maven Wrapper (no requiere Maven instalado)
./mvnw clean install -DskipTests
```

**Salida esperada:**

```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Build Order:
[INFO]
[INFO] Proyect01-Microservices                                     [pom]
[INFO] microservices.config                                        [jar]
[INFO] microservices.eureka                                        [jar]
[INFO] microservices.gateway                                       [jar]
[INFO] microservices.student                                       [jar]
[INFO] microservices.course                                        [jar]
[INFO]
...
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: XX.XXX s
```

> ⚠️ Si la compilación falla, revisa la sección de [Solución de Problemas](#solución-de-problemas-comunes).

---

## Ejecución de Servicios

> ⚡ **Orden crítico:** Los servicios DEBEN iniciarse en este orden específico.

### Opción A: Una Terminal por Servicio (Recomendado para desarrollo)

Abre **5 terminales separadas** y ejecuta un comando en cada una:

#### Terminal 1 — Config Server

```bash
cd microservices.config
mvn spring-boot:run
```

Espera ver:
```
Started Application in X.XXX seconds
Tomcat started on port 8888
```

#### Terminal 2 — Eureka Server

```bash
cd microservices.eureka
mvn spring-boot:run
```

Espera ver:
```
Started Application in X.XXX seconds
Tomcat started on port 8761
```

#### Terminal 3 — API Gateway

```bash
cd microservices.gateway
mvn spring-boot:run
```

Espera ver:
```
Started Application in X.XXX seconds
Tomcat started on port 8080
```

#### Terminal 4 — Student Service

```bash
cd microservices.student
mvn spring-boot:run
```

Espera ver:
```
Started Application in X.XXX seconds
Tomcat started on port 8090
Registering application MSVC-STUDENT with eureka
```

#### Terminal 5 — Course Service

```bash
cd microservices.course
mvn spring-boot:run
```

Espera ver:
```
Started Application in X.XXX seconds
Tomcat started on port 9090
Registering application MSVC-COURSE with eureka
```

---

### Opción B: Usando JARs Compilados

```bash
# Después de compilar con mvn clean install
java -jar microservices.config/target/microservices.config-0.0.1-SNAPSHOT.jar &
sleep 10
java -jar microservices.eureka/target/microservices.eureka-0.0.1-SNAPSHOT.jar &
sleep 10
java -jar microservices.gateway/target/microservices.gateway-0.0.1-SNAPSHOT.jar &
sleep 10
java -jar microservices.student/target/microservices.student-0.0.1-SNAPSHOT.jar &
java -jar microservices.course/target/microservices.course-0.0.1-SNAPSHOT.jar &
```

---

## Verificación del Sistema

### 1. Dashboard de Eureka

Abre en el navegador: **http://localhost:8761**

Deberías ver el dashboard de Eureka con los servicios registrados:

```
Instances currently registered with Eureka:
─────────────────────────────────────────────────────────
Application    AMIs    Availability Zones    Status
─────────────────────────────────────────────────────────
MSVC-COURSE    n/a     (1)                   UP(1)
MSVC-GATEWAY   n/a     (1)                   UP(1)
MSVC-STUDENT   n/a     (1)                   UP(1)
─────────────────────────────────────────────────────────
```

> Si no ves los servicios, espera 30 segundos y recarga la página (los heartbeats tienen delay).

### 2. Health Checks

```bash
# Verificar todos los servicios
curl http://localhost:8888/actuator/health  # Config
curl http://localhost:8761/actuator/health  # Eureka
curl http://localhost:8080/actuator/health  # Gateway
curl http://localhost:8090/actuator/health  # Student
curl http://localhost:9090/actuator/health  # Course
```

Todos deben responder: `{"status":"UP"}`

---

## Prueba del Sistema

Una vez todos los servicios estén corriendo, prueba el flujo completo:

```bash
# 1. Crear un curso
curl -X POST http://localhost:8080/api/course/create \
  -H "Content-Type: application/json" \
  -d '{"name": "Microservicios con Spring", "teacher": "Dr. Smith"}'

# 2. Crear un estudiante en ese curso
curl -X POST http://localhost:8080/api/student/create \
  -H "Content-Type: application/json" \
  -d '{"name": "Ana", "lastname": "Torres", "email": "ana@uni.edu", "courseId": 1}'

# 3. Ver el curso con sus estudiantes (llama internamente al Student Service)
curl http://localhost:8080/api/course/search_students/1
```

**Respuesta esperada del paso 3:**

```json
{
  "courseName": "Microservicios con Spring",
  "teacher": "Dr. Smith",
  "studentDTOList": [
    {
      "name": "Ana",
      "lastname": "Torres",
      "email": "ana@uni.edu",
      "courseId": 1
    }
  ]
}
```

¡Si ves esta respuesta, el sistema está funcionando correctamente! 🎉

---

## Solución de Problemas Comunes

### ❌ Error: `Connection refused: localhost:3306`

**Causa:** MySQL no está corriendo.

**Solución:**
```bash
# Linux
sudo systemctl start mysql
sudo systemctl status mysql

# macOS
brew services start mysql

# Windows: Buscar "MySQL" en Servicios de Windows y iniciarlo
```

---

### ❌ Error: `Connection refused: localhost:5432`

**Causa:** PostgreSQL no está corriendo.

**Solución:**
```bash
# Linux
sudo systemctl start postgresql
sudo systemctl status postgresql

# macOS
brew services start postgresql@15
```

---

### ❌ Error: `Access denied for user 'root'@'localhost'`

**Causa:** Contraseña incorrecta o usuario sin permisos.

**Solución:**
```bash
# Resetear contraseña root de MySQL
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'nueva_contraseña';
FLUSH PRIVILEGES;
EXIT;
```

Luego actualiza la contraseña en `application.yml` del Student Service.

---

### ❌ Error: `Unknown database 'students_db'`

**Causa:** La base de datos no fue creada.

**Solución:**
```bash
mysql -u root -p -e "CREATE DATABASE students_db CHARACTER SET utf8mb4;"
```

---

### ❌ Error: `FATAL: database "course_db" does not exist`

**Causa:** La base de datos PostgreSQL no fue creada.

**Solución:**
```bash
sudo -u postgres createdb course_db
# O
psql -U postgres -c "CREATE DATABASE course_db;"
```

---

### ❌ Error: El servicio no aparece en Eureka

**Causa:** El servicio se inició antes que el Eureka Server o hay un problema de conectividad.

**Solución:**
1. Asegúrate de que Eureka Server (`:8761`) está completamente iniciado
2. Reinicia el servicio problemático
3. Espera 30 segundos y recarga el dashboard de Eureka
4. Verifica la configuración de `eureka.client.service-url.defaultZone`

---

### ❌ Error: `Port 8080 already in use`

**Causa:** Otro proceso está usando el puerto.

**Solución:**
```bash
# Linux/Mac: Encontrar y matar el proceso
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

---

### ❌ La llamada a `/api/course/search_students/1` retorna lista vacía de estudiantes

**Causas posibles:**
1. No hay estudiantes creados con `courseId: 1`
2. El Student Service no está corriendo
3. El Feign Client no puede comunicarse con el Student Service

**Diagnóstico:**
```bash
# Verificar que el Student Service está corriendo
curl http://localhost:8090/actuator/health

# Verificar que hay estudiantes en el curso
curl http://localhost:8080/api/student/serach_by_id_course/1

# Verificar que el Gateway enruta correctamente
curl http://localhost:8080/api/student/all
```

---

### ❌ Error de compilación: `Could not find artifact`

**Causa:** Dependencias de Maven no descargadas.

**Solución:**
```bash
# Limpiar caché de Maven y recompilar
mvn dependency:purge-local-repository
mvn clean install -DskipTests
```

---

### ⚙️ Problema: Servicios lentos al iniciar

**Causa normal:** Spring Boot tarda algunos segundos en inicializar todo el ecosistema de Spring Cloud.

**Tiempos aproximados de inicio:**
- Config Server: ~5 segundos
- Eureka Server: ~10 segundos
- API Gateway: ~15 segundos
- Business Services: ~15-20 segundos

**Tip:** Agrega `-Dspring-boot.run.jvmArguments="-Xms128m -Xmx512m"` para limitar el uso de memoria si tu máquina tiene poca RAM.

---

## Configuración de IDE

### IntelliJ IDEA (Recomendado)

1. Abre el proyecto: **File → Open → Seleccionar carpeta raíz del proyecto**
2. IntelliJ detectará automáticamente el `pom.xml` padre y cargará todos los módulos
3. Instalar el plugin de **Lombok**: **Settings → Plugins → Buscar "Lombok"**
4. Habilitar procesamiento de anotaciones: **Settings → Build → Compiler → Annotation Processors → Enable annotation processing**
5. Para ejecutar servicios, usa la configuración de **Spring Boot** en el panel de ejecución

### VS Code

1. Instalar extensiones: **Extension Pack for Java**, **Spring Boot Extension Pack**
2. Abrir la carpeta raíz del proyecto
3. VS Code detectará automáticamente los módulos Maven

### Eclipse / Spring Tool Suite (STS)

1. **File → Import → Maven → Existing Maven Projects**
2. Seleccionar la carpeta raíz del proyecto
3. Importar todos los módulos detectados
4. Instalar el plugin de Lombok desde [projectlombok.org](https://projectlombok.org/setup/eclipse)
