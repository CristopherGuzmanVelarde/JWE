# Demo JWE - Cifrado y Descifrado entre Servicios

Esta demo muestra la comunicación segura entre dos servicios backend utilizando JWE (JSON Web Encryption) con Java 17.

## 📋 Descripción

La demo implementa:
- **Servicio 1** (Puerto 8081): Backend que cifra datos sensibles usando JWE y los envía al Servicio 2
- **Servicio 2** (Puerto 8082): Backend que recibe y descifra los datos encriptados

## 🏗️ Arquitectura

```
┌─────────────────────────┐          JWE          ┌─────────────────────┐
│   Servicio 1            │  ─────────────────>   │   Servicio 2        │
│ (Backend + Frontend)    │     (Cifrado)         │   (Backend)         │
│   Puerto: 8081          │                       │   Puerto: 8082      │
└─────────────────────────┘                       └─────────────────────┘
```

## 🔐 Características de Seguridad

- Algoritmo de cifrado: **RSA-OAEP-256** (key encryption)
- Algoritmo de contenido: **A256GCM** (content encryption)
- Claves RSA de 2048 bits
- Configuraciones externalizadas (no credenciales expuestas)
- Variables de entorno para información sensible

## 📦 Requisitos

- Java 17 o superior
- Maven 3.6+
- Puerto 8081 y 8082 disponibles

## 🚀 Guía de Uso

### Paso 1: Clonar o descargar el proyecto

```bash
cd c:\Users\Cristopher G\Desktop\JWE
```

### Paso 2: Generar las claves RSA (primera vez)

```bash
cd servicio1
mvn spring-boot:run -Dspring-boot.run.profiles=keygen
```

Esto generará:
- `keys/private-key.pem` - Clave privada (para Servicio 2)
- `keys/public-key.pem` - Clave pública (para Servicio 1)

**Nota**: Las claves se generan automáticamente si no existen. En producción, estas deben almacenarse en un sistema de gestión de secretos como Azure Key Vault, AWS Secrets Manager, etc.

### Paso 3: Copiar la clave privada al Servicio 2

```bash
copy servicio1\keys\private-key.pem servicio2\keys\private-key.pem
```

### Paso 4: Iniciar el Servicio 2 (Backend - Descifrado)

Abrir una nueva terminal:

```bash
cd servicio2
mvn clean install
mvn spring-boot:run
```

El servicio estará disponible en: `http://localhost:8082`

### Paso 5: Iniciar el Servicio 1 (Backend - Cifrado)

Abrir otra terminal:

```bash
cd servicio1
mvn clean install
mvn spring-boot:run
```

El servicio estará disponible en: `http://localhost:8081`

### Paso 6: Probar la demo

#### Opción A: Usando el navegador web
1. Abrir: `http://localhost:8081`
2. Completar el formulario con datos de prueba
3. Hacer clic en "Enviar Datos Cifrados"
4. Ver la respuesta del Servicio 2 con los datos descifrados

#### Opción B: Usando cURL

```bash
curl -X POST http://localhost:8081/api/encrypt-and-send ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Juan Perez\",\"email\":\"juan@example.com\",\"cardNumber\":\"4532-1234-5678-9010\"}"
```

#### Opción C: Probar directamente el cifrado

```bash
curl -X POST http://localhost:8081/api/encrypt ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Maria Garcia\",\"email\":\"maria@example.com\",\"cardNumber\":\"5555-6666-7777-8888\"}"
```

### Paso 7: Verificar logs

En ambas terminales podrás ver:
- **Servicio 1**: Logs del proceso de cifrado
- **Servicio 2**: Logs del proceso de descifrado

## 📁 Estructura del Proyecto

```
JWE/
├── servicio1/                      # Servicio de Cifrado
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/demo/jwe/
│   │   │   │       ├── config/        # Configuración de seguridad
│   │   │   │       ├── controller/    # Endpoints REST
│   │   │   │       ├── model/         # DTOs
│   │   │   │       ├── service/       # Lógica de cifrado
│   │   │   │       └── util/          # Utilidades
│   │   │   └── resources/
│   │   │       ├── application.yml    # Configuración
│   │   │       └── static/            # Frontend HTML
│   │   └── keys/                      # Claves RSA (generadas)
│   └── pom.xml
├── servicio2/                      # Servicio de Descifrado
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/demo/jwe/
│   │   │   │       ├── config/        # Configuración de seguridad
│   │   │   │       ├── controller/    # Endpoints REST
│   │   │   │       ├── model/         # DTOs
│   │   │   │       ├── service/       # Lógica de descifrado
│   │   │   │       └── util/          # Utilidades
│   │   │   └── resources/
│   │   │       └── application.yml    # Configuración
│   │   └── keys/                      # Clave privada (copiada)
│   └── pom.xml
└── README.md                       # Esta guía
```

## 🔧 Configuración

### Variables de Entorno (Opcional)

Puedes sobrescribir las configuraciones usando variables de entorno:

**Servicio 1:**
```bash
set JWE_PUBLIC_KEY_PATH=ruta/a/public-key.pem
set SERVER_PORT=8081
mvn spring-boot:run
```

**Servicio 2:**
```bash
set JWE_PRIVATE_KEY_PATH=ruta/a/private-key.pem
set SERVER_PORT=8082
mvn spring-boot:run
```

## 🧪 Endpoints Disponibles

### Servicio 1 (Puerto 8081)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Interfaz web de prueba |
| POST | `/api/encrypt` | Cifra datos y retorna el JWE |
| POST | `/api/encrypt-and-send` | Cifra y envía al Servicio 2 |
| GET | `/api/health` | Health check |

### Servicio 2 (Puerto 8082)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/decrypt` | Recibe y descifra un JWE |
| GET | `/api/health` | Health check |

## 🔍 Ejemplo de Flujo Completo

1. **Cliente envía datos al Servicio 1**:
```json
{
  "name": "Juan Perez",
  "email": "juan@example.com",
  "cardNumber": "4532-1234-5678-9010"
}
```

2. **Servicio 1 cifra con JWE**:
```
eyJhbGciOiJSU0EtT0FFUC0yNTYiLCJlbmMiOiJBMjU2R0NNIn0.X8f4Xq...
```

3. **Servicio 1 envía al Servicio 2**:
```json
{
  "encryptedData": "eyJhbGciOiJSU0EtT0FFUC0yNTYi..."
}
```

4. **Servicio 2 descifra y retorna**:
```json
{
  "status": "success",
  "decryptedData": {
    "name": "Juan Perez",
    "email": "juan@example.com",
    "cardNumber": "4532-1234-5678-9010"
  }
}
```

## ⚠️ Consideraciones de Seguridad

1. **Claves privadas**: Nunca commitear las claves en el repositorio
2. **Producción**: Usar un Key Management System (KMS)
3. **HTTPS**: En producción, siempre usar HTTPS
4. **Rotación**: Implementar rotación periódica de claves
5. **Logs**: No loguear datos sensibles descifrados

## 🐛 Troubleshooting

### Error: "Puerto ya en uso"
```bash
# Windows: Matar proceso en puerto 8081
netstat -ano | findstr :8081
taskkill /PID <PID> /F
```

### Error: "No se encuentra la clave"
Verificar que las claves existan en:
- `servicio1/keys/public-key.pem`
- `servicio2/keys/private-key.pem`

### Error: "Error al descifrar"
Asegurarse de que ambos servicios usen el mismo par de claves RSA.

## 📚 Tecnologías Utilizadas

- **Spring Boot 3.2.0** - Framework
- **Nimbus JOSE+JWT 9.37** - Implementación JWE
- **Maven** - Gestión de dependencias
- **Java 17** - Lenguaje de programación

## 📄 Licencia

Este es un proyecto de demostración con fines educativos.

## 👤 Autor

Demo creada para ilustrar el uso de JWE en comunicación entre servicios.
