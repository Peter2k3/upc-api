# 🚀 **API UPC - Sistema de Gestión Universitaria**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## � **Descripción**

API RESTful desarrollada en Spring Boot para la gestión integral de una universidad. Incluye funcionalidades para la gestión de publicaciones de blog, avisos, usuarios, roles, permisos, imágenes y estructura organizacional.

## ✨ **Características Principales**

- 🔐 **Autenticación JWT** con roles y permisos granulares
- 📝 **Gestión de Blog** con editor JSON y estados de publicación
- 📢 **Sistema de Avisos** con tipos y prioridades
- 🖼️ **Gestión de Imágenes** con integración a Strapi CMS
- 👥 **Administración de Usuarios** con roles dinámicos
- 🏢 **Estructura Organizacional** (personas, puestos, departamentos)
- 🔒 **Seguridad Robusta** con Spring Security
- 📊 **Base de Datos Optimizada** con relaciones complejas
- 🚀 **Arquitectura Escalable** con patrón MVC

## �️ **Tecnologías Utilizadas**

### **Backend**
- **Spring Boot 3.4.2** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM
- **Spring Validation** - Validación de datos
- **Spring Web** - API REST

### **Base de Datos**
- **PostgreSQL** - Base de datos principal
- **H2** - Base de datos en memoria para tests
- **Neon.tech** - Hosting de base de datos

### **Seguridad**
- **JWT (JSON Web Tokens)** - Autenticación
- **BCrypt** - Encriptación de contraseñas
- **CORS** - Control de acceso cross-origin

### **Testing**
- **JUnit 5** - Framework de testing
- **Mockito** - Mocking para tests unitarios
- **Spring Boot Test** - Tests de integración
- **TestContainers** - Tests con contenedores

### **Herramientas**
- **Maven** - Gestión de dependencias
- **Lombok** - Reducción de código boilerplate
- **Jackson** - Serialización JSON
- **Dotenv** - Gestión de variables de entorno

## 🏗️ **Arquitectura del Sistema**

### **Estructura del Proyecto**
```
src/
├── main/
│   ├── java/upc/api/
│   │   ├── controller/     # Controladores REST
│   │   ├── service/        # Lógica de negocio
│   │   ├── repository/     # Acceso a datos
│   │   ├── model/          # Entidades JPA
│   │   ├── security/       # Configuración de seguridad
│   │   ├── config/         # Configuraciones
│   │   ├── mapper/         # DTOs y mappers
│   │   └── utils/          # Utilidades
│   └── resources/
│       ├── application.properties
│       ├── banner.txt
│       └── keys/           # Claves JWT
└── test/
    ├── java/               # Tests unitarios e integración
    └── resources/          # Recursos para tests
```

### **Capas de la Aplicación**
1. **Controller Layer** - Endpoints REST y manejo de requests
2. **Service Layer** - Lógica de negocio y validaciones
3. **Repository Layer** - Acceso a datos y consultas
4. **Model Layer** - Entidades y DTOs
5. **Security Layer** - Autenticación y autorización

## 🚀 **Instalación y Configuración**

### **Requisitos Previos**
- Java 21 o superior
- Maven 3.6+
- PostgreSQL 15+
- Git

### **1. Clonar el Repositorio**
```bash
git clone <repository-url>
cd api
```

### **2. Configurar Variables de Entorno**
Crear archivo `.env` en la raíz del proyecto:
```env
# Base de Datos
DB_URL=jdbc:postgresql://localhost:5432/upc_db
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password

# JWT Configuration
JWT_SECRET_KEY=tu_clave_secreta_muy_larga_y_segura
JWT_TIME_EXPIRATION=86400000

# Strapi Configuration
STRAPI_BASE_URL=https://tu-strapi-instance.com
STRAPI_API_TOKEN=tu_token_de_strapi
```

### **3. Configurar Base de Datos**
```sql
-- Crear base de datos
CREATE DATABASE upc_db;

-- Crear usuario (opcional)
CREATE USER upc_user WITH PASSWORD 'tu_password';
GRANT ALL PRIVILEGES ON DATABASE upc_db TO upc_user;
```

### **4. Instalar Dependencias**
```bash
mvn clean install
```

### **5. Ejecutar la Aplicación**
```bash
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080`

## 🧪 **Testing**

### **Ejecutar Tests Unitarios**
```bash
mvn test -Dtest="*ServiceTest"
```

### **Ejecutar Tests de Integración**
```bash
mvn test -Dtest="*IntegrationTest"
```

### **Ejecutar Todos los Tests**
```bash
mvn test
```

### **Generar Reporte de Cobertura**
```bash
mvn jacoco:report
```

## 📚 **Documentación de la API**

### **Documentación Completa**
Ver [API_DOCUMENTATION.md](API_DOCUMENTATION.md) para la documentación completa de endpoints.

### **Ejemplos de Uso**

#### **Autenticación**
```bash
# Login
curl -X POST http://localhost:8080/upc/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@upc.edu","password":"admin123"}'

# Registro
curl -X POST http://localhost:8080/upc/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"nuevo@upc.edu","password":"123456","nombres":"Juan","paterno":"Pérez"}'
```

#### **Gestión de Blog**
```bash
# Obtener publicaciones públicas
curl -X GET http://localhost:8080/api/blog-posts

# Crear publicación (requiere autenticación)
curl -X POST http://localhost:8080/api/blog-posts \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Mi Post","slug":"mi-post","contenido":"{}","estado":"PUBLICADO"}'
```

#### **Gestión de Roles**
```bash
# Obtener roles (requiere autenticación)
curl -X GET http://localhost:8080/api/roles \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Crear rol
curl -X POST http://localhost:8080/api/roles \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"EDITOR","description":"Editor de contenido"}'
```

## 🗄️ **Modelo de Base de Datos**

### **Entidades Principales**
- **User** - Usuarios del sistema
- **Role** - Roles de usuario
- **Permission** - Permisos granulares
- **BlogPost** - Publicaciones del blog
- **Notice** - Avisos y notificaciones
- **Image** - Gestión de imágenes
- **Person** - Personas de la organización
- **Position** - Puestos de trabajo
- **PersonPosition** - Relación persona-puesto

### **Relaciones Clave**
- Usuario tiene un Rol (One-to-One)
- Rol tiene múltiples Permisos (Many-to-Many)
- Publicación tiene Autor (Many-to-One)
- Publicación tiene Imágenes (One-to-Many)
- Persona tiene múltiples Puestos (Many-to-Many)

## 🔒 **Seguridad**

### **Autenticación**
- JWT tokens con expiración configurable
- Passwords encriptados con BCrypt
- Validación de tokens en cada request

### **Autorización**
- Control de acceso basado en roles (RBAC)
- Permisos granulares por recurso y acción
- Anotaciones @PreAuthorize en endpoints

### **Medidas de Seguridad**
- Validación de entrada en todos los endpoints
- Protección CSRF deshabilitada para API REST
- Headers de seguridad configurados
- Soft delete para preservar integridad

## 🚀 **Deployment**

### **Desarrollo Local**
```bash
mvn spring-boot:run
```

### **Producción con JAR**
```bash
mvn clean package
java -jar target/api-0.0.1-SNAPSHOT.jar
```

### **Docker (Futuro)**
```dockerfile
# Dockerfile de ejemplo
FROM openjdk:21-jre-slim
COPY target/api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 📊 **Monitoreo y Observabilidad**

### **Health Checks**
```bash
curl http://localhost:8080/actuator/health
```

### **Métricas**
```bash
curl http://localhost:8080/actuator/metrics
```

### **Logs**
Los logs se configuran en `application.properties`:
```properties
logging.level.upc.api=DEBUG
logging.file.name=logs/api.log
```

## 🤝 **Contribución**

### **Proceso de Desarrollo**
1. Fork del repositorio
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit de cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

### **Estándares de Código**
- Seguir convenciones de Java y Spring Boot
- Documentar métodos públicos
- Escribir tests para nueva funcionalidad
- Mantener cobertura de tests > 80%

### **Commits**
Usar formato conventional commits:
```
feat: agregar endpoint de búsqueda de usuarios
fix: corregir validación de email
docs: actualizar documentación de API
test: agregar tests para UserService
```

## 🐛 **Troubleshooting**

### **Problemas Comunes**

#### **Error de Conexión a Base de Datos**
```
Verificar:
- Configuración en application.properties
- Variables de entorno .env
- Estado del servicio PostgreSQL
```

#### **JWT Token Inválido**
```
Verificar:
- Token no expirado
- Header Authorization correcto
- Clave secreta configurada
```

#### **Tests Fallando**
```
Ejecutar:
mvn clean install
mvn test -X (para debug)
```

## 📈 **Roadmap**

### **v1.1 (Próxima Versión)**
- [ ] Paginación en endpoints de listado
- [ ] Filtros avanzados de búsqueda
- [ ] Cache con Redis
- [ ] Notificaciones en tiempo real (WebSocket)

### **v2.0 (Futuro)**
- [ ] API GraphQL
- [ ] Microservicios
- [ ] Event Sourcing
- [ ] Multi-tenancy

## 📄 **Licencia**

Este proyecto está bajo la Licencia MIT. Ver [LICENSE](LICENSE) para más detalles.

## 👥 **Equipo**

- **Peter2k3** - Desarrollador Principal
- **UPC Team** - Stakeholders y Testing

## 📞 **Contacto**

Para preguntas o soporte:
- Email: soporte@upc.edu
- Issues: GitHub Issues
- Documentación: [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

---

**Desarrollado con ❤️ para UPC**

*Última actualización: Julio 3, 2025*
- Publicación tiene Autor (Many-to-One)
- Publicación tiene Imágenes (One-to-Many)
- Persona tiene múltiples Puestos (Many-to-Many)
│   └── [otros controllers]
├── model/
│   ├── dto/
│   │   ├── LoginDTO.java      # DTO para login
│   │   └── UserRegisterDTO.java # DTO para registro
│   ├── User.java             # Entidad Usuario
│   └── [otras entidades]
├── security/
│   ├── SecurityConfig.java        # Configuración de seguridad
│   ├── JWTAuthenticationFilter.java # Filtro de autenticación
│   ├── JWTAuthorizationFilter.java  # Filtro de autorización
│   ├── CustomUserDetails.java      # UserDetails personalizado
│   └── CustomUserDetailsService.java # Servicio UserDetails
├── service/
│   ├── IJWTUtilityService.java    # Interfaz servicio JWT
│   └── impl/
│       ├── JWTServiceImpl.java    # Implementación servicio JWT
│       └── AuthServiceImpl.java   # Implementación servicio Auth
└── repository/                # Repositorios JPA
```

## 🔐 Configuración JWT

### Claves RSA
Las claves RSA están ubicadas en `src/main/resources/keys/`:
- `private_key.pem` - Clave privada para firmar tokens
- `public_key.pem` - Clave pública para verificar tokens

### Configuración en application.properties
```properties
# Configuración JWT
jwtKeys.privateKeyPath=keys/private_key.pem
jwtKeys.publicKeyPath=keys/public_key.pem

# Base de datos PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/upc
spring.datasource.username=postgres
spring.datasource.password=root
```

## 🚀 Endpoints Disponibles

### Autenticación
- **POST** `/upc/auth/login` - Iniciar sesión
- **POST** `/upc/auth/register` - Registrar usuario

### Pruebas
- **GET** `/upc/api/public` - Endpoint público (sin autenticación)
- **GET** `/upc/api/private` - Endpoint privado (requiere JWT)
- **GET** `/upc/api/test-jwt` - Prueba de JWT

## 📝 Ejemplos de Uso

### 1. Registro de Usuario
```bash
curl -X POST http://localhost:8080/upc/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan",
    "lastname": "Pérez",
    "email": "juan@example.com",
    "password": "password123",
    "roles": []
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/upc/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@example.com",
    "password": "password123"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjg4..."
}
```

### 3. Acceso a Endpoint Protegido
```bash
curl -X GET http://localhost:8080/upc/api/private \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjg4..."
```

## 🔧 Implementación JWT

### Generación de Token
```java
@Override
public String generateJWT(Long userId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
    PrivateKey privateKey = loadPrivateKey(privateKeyResourse);
    JWSSigner signer = new RSASSASigner(privateKey);
    Date now = new Date();
    
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(userId.toString())
        .issueTime(now)
        .expirationTime(new Date(now.getTime() + 14400000)) // 4 horas
        .build();

    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
    signedJWT.sign(signer);
    return signedJWT.serialize();
}
```

### Validación de Token
```java
@Override
public JWTClaimsSet parseJWT(String jwt) throws InvalidKeySpecException, NoSuchAlgorithmException, ParseException, JOSEException, IOException {
    PublicKey publicKey = loadPublicKey(publicKeyResourse);
    SignedJWT signedJWT = SignedJWT.parse(jwt);
    JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);

    if (!signedJWT.verify(verifier)){
        throw new JOSEException("Invalid signature");
    }

    JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
    if (claimsSet.getExpirationTime().before(new Date())){
        throw new JOSEException("Expired token");
    }

    return claimsSet;
}
```

## 🛡️ Seguridad

### Filtros de Seguridad
1. **JWTAuthenticationFilter**: Procesa el login y genera JWT
2. **JWTAuthorizationFilter**: Valida JWT en cada request

### Configuración de Spring Security
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        JWTAuthenticationFilter authFilter = new JWTAuthenticationFilter(authManager, jwtUtilityService);
        authFilter.setFilterProcessesUrl("/upc/auth/login");

        return http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/upc/auth/**", "/upc/api/public").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilter(authFilter)
            .addFilterBefore(new JWTAuthorizationFilter(jwtUtilityService), JWTAuthenticationFilter.class)
            .build();
    }
}
```

## ⚙️ Configuración de Base de Datos
Asegúrate de tener PostgreSQL corriendo y crear la base de datos:
```sql
CREATE DATABASE upc;
```

## 🚀 Ejecutar la Aplicación
```bash
# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run
```

## 📋 Características Implementadas
✅ Autenticación JWT con RSA256  
✅ Registro e inicio de sesión  
✅ Filtros de seguridad personalizados  
✅ Validación de tokens  
✅ Endpoints protegidos y públicos  
✅ Integración con Spring Security  
✅ Gestión de usuarios con roles  
✅ Encriptación de contraseñas con BCrypt  

## 🔄 Flujo de Autenticación
1. Usuario envía credenciales a `/upc/auth/login`
2. Spring Security valida credenciales
3. Si son válidas, se genera JWT firmado con clave privada
4. Cliente recibe token JWT
5. Cliente incluye token en header `Authorization: Bearer <token>`
6. Filtro de autorización valida token con clave pública
7. Si token es válido, se permite acceso al recurso

## ⏰ Configuración de Expiración
- **Duración del token**: 4 horas (14400000 ms)
- **Algoritmo**: RS256 (RSA con SHA-256)
- **Formato**: Bearer Token en header Authorization
