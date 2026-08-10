# BiggApp

Aplicación web desarrollada con:

* React
* Spring Boot
* PostgreSQL
* Docker
* Docker Compose

## Variables de entorno

El proyecto incluye un archivo `.env` configurado para ejecutar la aplicación localmente con Docker Compose.

Las principales variables son:

```env
POSTGRES\\\\\\\_CONTAINER\\\\\\\_NAME=biggapp-postgres
POSTGRES\\\\\\\_DB=biggapp
POSTGRES\\\\\\\_USER=biggapp\\\\\\\_user
POSTGRES\\\\\\\_PASSWORD=\\\\\\\*\\\\\\\*\\\\\\\*\\\\\\\*\\\\\\\*\\\\\\\*\\\\\\\*\\\\\\\*
POSTGRES\\\\\\\_PORT=5432

BACKEND\\\\\\\_CONTAINER\\\\\\\_NAME=biggapp-backend
BACKEND\\\\\\\_PORT=8081
BACKEND\\\\\\\_INTERNAL\\\\\\\_PORT=8081

FRONTEND\\\\\\\_CONTAINER\\\\\\\_NAME=biggapp-frontend
FRONTEND\\\\\\\_PORT=5173

VITE\\\\\\\_API\\\\\\\_URL=http://localhost:8081/api/rest
VITE\\\\\\\_WS\\\\\\\_HOST=localhost:8081/api
```

## Ejecutar la aplicación

Desde la carpeta raíz del proyecto ejecutar:

```bash
docker compose up --build
```

Este comando construye las imágenes del backend y frontend y levanta los tres servicios.

```

## Acceso a los servicios

### Frontend

```text
http://localhost:5173/tallerfrontLJJJ/
```

### Backend

```text
http://localhost:8081/api
```

### PostgreSQL

```text
Host: localhost
Puerto: 5432
Base de datos: biggapp
Usuario: biggapp\\\\\\\_user
```

La contraseña se encuentra definida en el archivo `.env`.

## Usuarios de prueba

### Administrador

```text
Usuario: Admin Uno
Contraseña: password
```

### Entrenador

```text
Usuario: Trainer Uno
Contraseña: password
```

### Estudiante

```text
Usuario: Student Uno
Contraseña: password
```

## Verificar contenedores

```bash
docker compose ps
```

Deben estar activos los servicios:

* postgres
* backend
* frontend

## Detener la aplicación

```bash
docker compose down
```



