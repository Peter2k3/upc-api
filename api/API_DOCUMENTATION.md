# 📚 **Documentación Completa de la API UPC**

## 🔥 **Información General**

### **Base URL**
```
http://localhost:8080
```

### **Tecnologías**
- **Framework**: Spring Boot 3.4.2
- **Base de Datos**: PostgreSQL (Neon.tech)
- **Autenticación**: JWT + Spring Security
- **ORM**: Hibernate/JPA
- **Integración Externa**: Strapi CMS

### **Arquitectura**
- **Patrón**: MVC (Model-View-Controller)
- **Capas**: Controller → Service → Repository → Entity
- **Seguridad**: Autenticación basada en roles y permisos

---

## 🔐 **Autenticación y Autorización**

### **Endpoints de Autenticación**

#### **POST** `/upc/auth/login`
**Descripción**: Autentica un usuario y devuelve un token JWT.

**Request Body**:
```json
{
  "email": "usuario@ejemplo.com",
  "password": "contraseña123"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Códigos de Estado**:
- `200 OK`: Login exitoso
- `401 Unauthorized`: Credenciales incorrectas

---

#### **POST** `/upc/auth/register`
**Descripción**: Registra un nuevo usuario en el sistema.

**Request Body**:
```json
{
  "email": "nuevo@ejemplo.com",
  "password": "contraseña123",
  "nombres": "Juan",
  "paterno": "Pérez",
  "materno": "García"
}
```

**Response**:
```json
{
  "message": "Usuario registrado exitosamente"
}
```

**Códigos de Estado**:
- `200 OK`: Registro exitoso
- `400 Bad Request`: Datos inválidos
- `409 Conflict`: El email ya existe

---

## 📝 **Gestión de Publicaciones del Blog**

### **Endpoints Públicos**

#### **GET** `/api/blog-posts`
**Descripción**: Obtiene todas las publicaciones publicadas.

**Response**:
```json
[
  {
    "id": 1,
    "titulo": "Mi primera publicación",
    "slug": "mi-primera-publicacion",
    "contenido": "{\"blocks\":[...]}",
    "estado": "PUBLICADO",
    "fechaPublicacion": "2025-01-15T10:00:00",
    "createdAt": "2025-01-10T08:00:00",
    "author": {
      "id": 1,
      "nombres": "Juan",
      "paterno": "Pérez"
    },
    "imagenDestacada": {
      "id": 1,
      "url": "https://strapi.com/imagen1.jpg",
      "altText": "Imagen destacada"
    }
  }
]
```

---

#### **GET** `/api/blog-posts/{id}`
**Descripción**: Obtiene una publicación específica por ID.

**Parámetros**:
- `id` (Long): ID de la publicación

**Response**: Objeto `BlogPost` (igual al anterior)

**Códigos de Estado**:
- `200 OK`: Publicación encontrada
- `404 Not Found`: Publicación no existe

---

#### **GET** `/api/blog-posts/slug/{slug}`
**Descripción**: Obtiene una publicación por su slug único.

**Parámetros**:
- `slug` (String): Slug de la publicación

**Response**: Objeto `BlogPost`

---

#### **GET** `/api/blog-posts/featured`
**Descripción**: Obtiene las publicaciones destacadas.

**Response**: Array de objetos `BlogPost`

---

#### **GET** `/api/blog-posts/search?q={query}`
**Descripción**: Busca publicaciones por título o contenido.

**Query Parameters**:
- `q` (String): Término de búsqueda

**Response**: Array de objetos `BlogPost`

---

### **Endpoints Administrativos** (Requieren Autenticación)

#### **POST** `/api/blog-posts`
**Descripción**: Crea una nueva publicación.
**Permisos**: `CREATE_POSTS`

**Request Body**:
```json
{
  "titulo": "Nueva publicación",
  "slug": "nueva-publicacion",
  "contenido": "{\"blocks\":[{\"type\":\"paragraph\",\"data\":{\"text\":\"Contenido...\"}}]}",
  "estado": "BORRADOR",
  "fechaPublicacion": "2025-01-20T10:00:00",
  "imagenDestacadaId": 1
}
```

**Response**: Objeto `BlogPost` creado

**Códigos de Estado**:
- `201 Created`: Publicación creada
- `409 Conflict`: El slug ya existe
- `403 Forbidden`: Sin permisos

---

#### **PUT** `/api/blog-posts/{id}`
**Descripción**: Actualiza una publicación existente.
**Permisos**: `UPDATE_POSTS`

**Request Body**: Igual al POST

**Códigos de Estado**:
- `200 OK`: Publicación actualizada
- `404 Not Found`: Publicación no existe

---

#### **DELETE** `/api/blog-posts/{id}`
**Descripción**: Elimina (soft delete) una publicación.
**Permisos**: `DELETE_POSTS`

**Códigos de Estado**:
- `204 No Content`: Eliminación exitosa
- `404 Not Found`: Publicación no existe

---

#### **PUT** `/api/blog-posts/{id}/publish`
**Descripción**: Publica una publicación.
**Permisos**: `PUBLISH_POSTS`

**Códigos de Estado**:
- `200 OK`: Publicación publicada
- `400 Bad Request`: No se pudo publicar

---

#### **PUT** `/api/blog-posts/{id}/unpublish`
**Descripción**: Despublica una publicación.
**Permisos**: `PUBLISH_POSTS`

---

#### **GET** `/api/blog-posts/admin`
**Descripción**: Obtiene todas las publicaciones para administración.
**Permisos**: `READ_ALL_POSTS`

---

#### **GET** `/api/blog-posts/by-estado/{estado}`
**Descripción**: Obtiene publicaciones por estado.
**Permisos**: `READ_ALL_POSTS`

**Parámetros**:
- `estado`: `BORRADOR`, `PUBLICADO`, `ARCHIVADO`

---

## 🔒 **Gestión de Roles y Permisos**

### **Endpoints de Roles**

#### **GET** `/api/roles`
**Descripción**: Obtiene todos los roles activos.
**Permisos**: `READ_ROLES`

**Response**:
```json
[
  {
    "id": 1,
    "name": "ADMIN",
    "description": "Administrador del sistema",
    "active": true,
    "createdAt": "2025-01-01T00:00:00",
    "permissions": [
      {
        "id": 1,
        "name": "CREATE_POSTS",
        "description": "Crear publicaciones",
        "resource": "posts",
        "action": "create"
      }
    ]
  }
]
```

---

#### **GET** `/api/roles/{id}`
**Descripción**: Obtiene un rol específico.
**Permisos**: `READ_ROLES`

---

#### **POST** `/api/roles`
**Descripción**: Crea un nuevo rol.
**Permisos**: `CREATE_ROLES`

**Request Body**:
```json
{
  "name": "EDITOR",
  "description": "Editor de contenido"
}
```

---

#### **PUT** `/api/roles/{id}`
**Descripción**: Actualiza un rol.
**Permisos**: `UPDATE_ROLES`

---

#### **DELETE** `/api/roles/{id}`
**Descripción**: Elimina un rol.
**Permisos**: `DELETE_ROLES`

---

#### **POST** `/api/roles/{roleId}/permissions/{permissionId}`
**Descripción**: Asigna un permiso a un rol.
**Permisos**: `ASSIGN_PERMISSIONS`

---

#### **DELETE** `/api/roles/{roleId}/permissions/{permissionId}`
**Descripción**: Remueve un permiso de un rol.
**Permisos**: `REMOVE_PERMISSIONS`

---

#### **GET** `/api/roles/{id}/permissions`
**Descripción**: Obtiene los permisos de un rol.
**Permisos**: `READ_PERMISSIONS`

---

## 📢 **Gestión de Avisos/Notificaciones**

### **Endpoints Públicos**

#### **GET** `/api/notices`
**Descripción**: Obtiene avisos activos actuales.

**Response**:
```json
[
  {
    "id": 1,
    "titulo": "Mantenimiento programado",
    "contenido": "El sistema estará en mantenimiento...",
    "tipo": "INFORMACION",
    "prioridad": "MEDIA",
    "fechaInicio": "2025-01-15T09:00:00",
    "fechaFin": "2025-01-15T18:00:00",
    "fijado": true,
    "activo": true,
    "author": {
      "id": 1,
      "nombres": "Admin"
    },
    "imagen": {
      "id": 1,
      "url": "https://strapi.com/aviso.jpg"
    }
  }
]
```

---

#### **GET** `/api/notices/{id}`
**Descripción**: Obtiene un aviso específico.

---

#### **GET** `/api/notices/pinned`
**Descripción**: Obtiene el aviso fijado actual.

---

#### **GET** `/api/notices/by-type/{tipo}`
**Descripción**: Obtiene avisos por tipo.

**Tipos disponibles**:
- `INFORMACION`
- `ALERTA`
- `URGENTE`

---

#### **GET** `/api/notices/search?q={query}`
**Descripción**: Busca avisos por título o contenido.

---

### **Endpoints Administrativos**

#### **POST** `/api/notices`
**Descripción**: Crea un nuevo aviso.
**Permisos**: `CREATE_NOTICES`

**Request Body**:
```json
{
  "titulo": "Nuevo aviso",
  "contenido": "Contenido del aviso...",
  "tipo": "INFORMACION",
  "prioridad": "ALTA",
  "fechaInicio": "2025-01-20T09:00:00",
  "fechaFin": "2025-01-20T18:00:00",
  "fijado": false,
  "imagenId": 1
}
```

---

#### **PUT** `/api/notices/{id}`
**Descripción**: Actualiza un aviso.
**Permisos**: `UPDATE_NOTICES`

---

#### **DELETE** `/api/notices/{id}`
**Descripción**: Elimina un aviso.
**Permisos**: `DELETE_NOTICES`

---

#### **PUT** `/api/notices/{id}/pin`
**Descripción**: Fija un aviso.
**Permisos**: `PIN_NOTICES`

---

#### **PUT** `/api/notices/{id}/unpin`
**Descripción**: Desfija un aviso.
**Permisos**: `PIN_NOTICES`

---

## 🖼️ **Gestión de Imágenes**

### **Endpoints de Imágenes**

#### **GET** `/api/images`
**Descripción**: Obtiene todas las imágenes activas.
**Permisos**: `READ_IMAGES`

**Response**:
```json
[
  {
    "id": 1,
    "nombreOriginal": "imagen-blog.jpg",
    "nombreArchivo": "imagen-blog-1642781234567.jpg",
    "url": "https://strapi.com/uploads/imagen-blog-1642781234567.jpg",
    "idStrapi": "strapi-id-12345",
    "altText": "Imagen para blog",
    "mimeType": "image/jpeg",
    "tamanoBytes": 1024768,
    "createdAt": "2025-01-15T10:00:00",
    "uploadedBy": {
      "id": 1,
      "nombres": "Juan",
      "email": "juan@upc.edu"
    }
  }
]
```

---

#### **GET** `/api/images/{id}`
**Descripción**: Obtiene una imagen específica.

---

#### **GET** `/api/images/by-type/{tipo}`
**Descripción**: Obtiene imágenes por tipo.
**Permisos**: `READ_IMAGES`

---

#### **POST** `/api/images/upload`
**Descripción**: Sube una nueva imagen.
**Permisos**: `UPLOAD_IMAGES`

**Request**: Multipart Form
- `file`: Archivo de imagen
- `altText`: Texto alternativo (opcional)
- `tipo`: Tipo de imagen (default: "general")

**Response**: Objeto `Image` creado

---

#### **PUT** `/api/images/{id}`
**Descripción**: Actualiza metadatos de una imagen.
**Permisos**: `UPDATE_IMAGES`

---

#### **DELETE** `/api/images/{id}`
**Descripción**: Elimina una imagen.
**Permisos**: `DELETE_IMAGES`

---

#### **POST** `/api/images/sync-from-strapi`
**Descripción**: Sincroniza imágenes desde Strapi.
**Permisos**: `SYNC_IMAGES`

---

## 👥 **Gestión de Personas y Puestos**

### **Endpoints de Personas**

#### **GET** `/api/persons`
**Descripción**: Obtiene todas las personas activas.
**Permisos**: `READ_PERSONS`

**Response**:
```json
[
  {
    "id": 1,
    "nombres": "Juan Carlos",
    "paterno": "Pérez",
    "materno": "García",
    "dni": "12345678",
    "telefono": "+51987654321",
    "activo": true,
    "positions": [
      {
        "id": 1,
        "position": {
          "id": 1,
          "nombre": "Director",
          "departamento": "Administración"
        },
        "fechaInicio": "2025-01-01",
        "fechaFin": null,
        "activo": true
      }
    ]
  }
]
```

---

#### **GET** `/api/persons/{id}`
**Descripción**: Obtiene una persona específica.
**Permisos**: `READ_PERSONS`

---

#### **POST** `/api/persons`
**Descripción**: Crea una nueva persona.
**Permisos**: `CREATE_PERSONS`

**Request Body**:
```json
{
  "nombres": "María Elena",
  "paterno": "López",
  "materno": "Ruiz",
  "dni": "87654321",
  "telefono": "+51912345678"
}
```

---

#### **PUT** `/api/persons/{id}`
**Descripción**: Actualiza una persona.
**Permisos**: `UPDATE_PERSONS`

---

#### **DELETE** `/api/persons/{id}`
**Descripción**: Desactiva una persona.
**Permisos**: `DELETE_PERSONS`

---

### **Endpoints de Puestos**

#### **GET** `/api/positions`
**Descripción**: Obtiene todos los puestos activos.
**Permisos**: `READ_POSITIONS`

**Response**:
```json
[
  {
    "id": 1,
    "nombre": "Director Académico",
    "descripcion": "Responsable de la gestión académica",
    "departamento": "Académico",
    "active": true,
    "createdAt": "2025-01-01T00:00:00"
  }
]
```

---

#### **POST** `/api/positions`
**Descripción**: Crea un nuevo puesto.
**Permisos**: `CREATE_POSITIONS`

---

#### **PUT** `/api/positions/{id}`
**Descripción**: Actualiza un puesto.
**Permisos**: `UPDATE_POSITIONS`

---

#### **DELETE** `/api/positions/{id}`
**Descripción**: Desactiva un puesto.
**Permisos**: `DELETE_POSITIONS`

---

## 🔧 **Modelos de Datos**

### **Usuario (User)**
```json
{
  "id": "Long",
  "email": "String (único)",
  "passwordHash": "String",
  "nombres": "String",
  "paterno": "String",
  "materno": "String",
  "active": "Boolean",
  "notExpired": "Boolean",
  "notBlocked": "Boolean",
  "nonExpiredCredentials": "Boolean",
  "lastLogin": "LocalDateTime",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime",
  "role": "Role"
}
```

### **Publicación (BlogPost)**
```json
{
  "id": "Long",
  "titulo": "String",
  "slug": "String (único)",
  "contenido": "JSON",
  "estado": "EstadoPublicacion (BORRADOR|PUBLICADO|ARCHIVADO)",
  "fechaPublicacion": "LocalDateTime",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime",
  "author": "User",
  "imagenDestacada": "Image",
  "images": "List<PostImage>"
}
```

### **Aviso (Notice)**
```json
{
  "id": "Long",
  "titulo": "String",
  "contenido": "TEXT",
  "tipo": "TipoAviso (INFORMACION|ALERTA|URGENTE)",
  "prioridad": "Prioridad (BAJA|MEDIA|ALTA)",
  "fechaInicio": "LocalDateTime",
  "fechaFin": "LocalDateTime",
  "fijado": "Boolean",
  "activo": "Boolean",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime",
  "author": "User",
  "imagen": "Image"
}
```

### **Imagen (Image)**
```json
{
  "id": "Long",
  "nombreOriginal": "String",
  "nombreArchivo": "String",
  "url": "String",
  "idStrapi": "String",
  "altText": "String",
  "mimeType": "String",
  "tamanoBytes": "Long",
  "createdAt": "LocalDateTime",
  "uploadedBy": "User"
}
```

### **Rol (Role)**
```json
{
  "id": "Long",
  "name": "String (único)",
  "description": "String",
  "active": "Boolean",
  "createdAt": "LocalDateTime",
  "permissions": "List<Permission>"
}
```

### **Permiso (Permission)**
```json
{
  "id": "Long",
  "name": "String (único)",
  "description": "String",
  "resource": "String",
  "action": "String",
  "active": "Boolean",
  "createdAt": "LocalDateTime"
}
```

---

## 🔑 **Sistema de Permisos**

### **Permisos Disponibles**

#### **Publicaciones**
- `CREATE_POSTS` - Crear publicaciones
- `READ_POSTS` - Leer publicaciones
- `UPDATE_POSTS` - Actualizar publicaciones
- `DELETE_POSTS` - Eliminar publicaciones
- `PUBLISH_POSTS` - Publicar/despublicar
- `READ_ALL_POSTS` - Leer todas las publicaciones (admin)

#### **Avisos**
- `CREATE_NOTICES` - Crear avisos
- `READ_NOTICES` - Leer avisos
- `UPDATE_NOTICES` - Actualizar avisos
- `DELETE_NOTICES` - Eliminar avisos
- `PIN_NOTICES` - Fijar/desfijar avisos

#### **Imágenes**
- `READ_IMAGES` - Ver imágenes
- `UPLOAD_IMAGES` - Subir imágenes
- `UPDATE_IMAGES` - Actualizar imágenes
- `DELETE_IMAGES` - Eliminar imágenes
- `SYNC_IMAGES` - Sincronizar con Strapi

#### **Roles y Permisos**
- `READ_ROLES` - Ver roles
- `CREATE_ROLES` - Crear roles
- `UPDATE_ROLES` - Actualizar roles
- `DELETE_ROLES` - Eliminar roles
- `ASSIGN_PERMISSIONS` - Asignar permisos
- `REMOVE_PERMISSIONS` - Remover permisos
- `READ_PERMISSIONS` - Ver permisos

#### **Personas y Puestos**
- `READ_PERSONS` - Ver personas
- `CREATE_PERSONS` - Crear personas
- `UPDATE_PERSONS` - Actualizar personas
- `DELETE_PERSONS` - Eliminar personas
- `READ_POSITIONS` - Ver puestos
- `CREATE_POSITIONS` - Crear puestos
- `UPDATE_POSITIONS` - Actualizar puestos
- `DELETE_POSITIONS` - Eliminar puestos

---

## 🚀 **Códigos de Estado HTTP**

### **Éxito**
- `200 OK` - Solicitud exitosa
- `201 Created` - Recurso creado exitosamente
- `204 No Content` - Operación exitosa sin contenido

### **Error del Cliente**
- `400 Bad Request` - Datos inválidos en la solicitud
- `401 Unauthorized` - No autenticado
- `403 Forbidden` - Sin permisos suficientes
- `404 Not Found` - Recurso no encontrado
- `409 Conflict` - Conflicto (ej: email duplicado)

### **Error del Servidor**
- `500 Internal Server Error` - Error interno del servidor

---

## 🔧 **Headers Requeridos**

### **Autenticación**
```
Authorization: Bearer {jwt_token}
```

### **Content-Type**
```
Content-Type: application/json
```

Para uploads de archivos:
```
Content-Type: multipart/form-data
```

---

## 🌐 **CORS**

La API está configurada para aceptar solicitudes desde cualquier origen (`*`). En producción, esto debería ser más restrictivo.

---

## 📝 **Notas Importantes**

1. **JWT Tokens**: Los tokens JWT tienen una expiración configurada. Renovar según sea necesario.

2. **Soft Delete**: La mayoría de operaciones DELETE son "soft deletes" (marcan como inactivo).

3. **Paginación**: Algunos endpoints pueden beneficiarse de paginación en el futuro.

4. **Strapi Integration**: Las imágenes se sincronizan con Strapi CMS para gestión externa.

5. **Validaciones**: Todos los endpoints incluyen validaciones de datos y permisos.

6. **Logs**: El sistema incluye logging para auditoría y debugging.

---

## 🚀 **Comandos de Desarrollo**

### **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

### **Compilar**
```bash
mvn clean compile
```

### **Ejecutar tests**
```bash
mvn test
```

### **Crear package**
```bash
mvn clean package
```

---

## 📞 **Soporte**

Para soporte técnico o consultas sobre la API, contactar al equipo de desarrollo.

**Última actualización**: Julio 3, 2025  
**Versión de la API**: 1.0.0  
**Autor**: Peter2k3

---

*Esta documentación está en constante actualización. Consultar la última versión en el repositorio del proyecto.*
