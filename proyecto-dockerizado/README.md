# BiggApp - Spring Boot + React + PostgreSQL con Docker Compose

Este repositorio contiene la contenerización de la aplicación desarrollada para **Computación en Internet II**. La solución se ejecuta con tres servicios coordinados por Docker Compose:

- **PostgreSQL**: base de datos.
- **Spring Boot**: backend REST y WebSocket.
- **React + Vite**: frontend.

## Requisitos

Antes de iniciar, tener instalados:

- Docker Desktop (o Docker Engine + Docker Compose).
- Git, únicamente si se clonará el repositorio desde GitHub.

No es necesario instalar PostgreSQL, Java, Maven ni Node.js de forma local para ejecutar la aplicación con Docker.

## 1. Configurar variables de entorno

En la raíz del proyecto se incluye `.env.example`. Crear una copia llamada `.env`:

```bash
cp .env.example .env
```

En Windows PowerShell también puede usarse:

```powershell
Copy-Item .env.example .env
```

Editar `.env` y, como mínimo, cambiar los valores sensibles:

```env
POSTGRES_PASSWORD=change_me
JWT_SECRET_KEY=change_this_secret_key_to_at_least_64_characters_12345678901234567890
```

Las variables principales son:

| Variable | Propósito | Valor de ejemplo |
|---|---|---|
| `POSTGRES_DB` | Nombre de la base de datos | `biggapp` |
| `POSTGRES_USER` | Usuario PostgreSQL | `biggapp_user` |
| `POSTGRES_PASSWORD` | Contraseña PostgreSQL | `change_me` |
| `POSTGRES_PORT` | Puerto publicado de PostgreSQL | `5432` |
| `BACKEND_PORT` | Puerto del backend en el host | `8081` |
| `FRONTEND_PORT` | Puerto del frontend en el host | `5173` |
| `VITE_API_URL` | URL REST usada por el navegador | `http://localhost:8081/api/rest` |
| `VITE_WS_HOST` | Host/ruta WebSocket usada por el navegador | `localhost:8081/api` |
| `APP_CORS_ALLOWED_ORIGINS` | Orígenes permitidos por Spring Boot | `http://localhost:5173` |

> El backend accede a PostgreSQL usando el nombre del servicio de Docker Compose (`postgres`), no `localhost`.

## 2. Construir las imágenes

Desde la raíz del proyecto ejecutar:

```bash
docker compose build
```

También es posible construir y levantar los servicios en un solo comando:

```bash
docker compose up --build
```

## 3. Levantar la aplicación

```bash
docker compose up
```

Para ejecutarla en segundo plano:

```bash
docker compose up -d
```

## 4. Acceder a los servicios

Con los valores por defecto de `.env.example`:

- Frontend: http://localhost:5173
- Backend: http://localhost:8081/api
- API REST: http://localhost:8081/api/rest
- PostgreSQL: `localhost:5432`

La ruta de autenticación REST queda, por ejemplo, bajo:

```text
http://localhost:8081/api/rest/public/auth/login
```

## 5. Ver estado y logs

Ver los contenedores:

```bash
docker compose ps
```

Ver logs de todos los servicios:

```bash
docker compose logs -f
```

Ver únicamente el backend:

```bash
docker compose logs -f backend
```

## 6. Detener la aplicación

```bash
docker compose down
```

Para eliminar también el volumen de PostgreSQL:

```bash
docker compose down -v
```

## Estructura relevante

```text
.
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── frontend/
│   ├── Dockerfile
│   ├── package.json
│   └── src/
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

## Notas de configuración

El backend fue adaptado de H2 a PostgreSQL mediante variables de entorno. La configuración de Spring Boot se encuentra en `backend/src/main/resources/application.properties`.

El frontend usa las variables `VITE_API_URL` y `VITE_WS_HOST`. Estas se inyectan al contenedor desde Docker Compose.

La configuración incluida usa `SPRING_JPA_HIBERNATE_DDL_AUTO=create` y `SPRING_SQL_INIT_MODE=always` para que Hibernate cree el esquema y posteriormente cargue `data.sql` al iniciar el backend. Esto facilita la ejecución académica con los datos iniciales del proyecto.
