# BackendQMA Microservices Monorepo

This repository contains the backend microservices for the Quantity Measurement Application.

## Services

- `discovery-server` (Eureka)
- `api-gateway` (Spring Cloud Gateway)
- `auth-service` (JWT + OAuth2 login)
- `user-service` (user APIs)
- `measurement-service` (quantity measurement APIs)

## Ports

- Discovery Server: `8761`
- API Gateway: `8080`
- Auth Service: `8081`
- User Service: `8082`
- Measurement Service: `8083`

## Start Order (Important)

1. `discovery-server`
2. `auth-service`, `user-service`, `measurement-service`
3. `api-gateway`

## Run Commands

Open separate terminals from repo root and run:

```powershell
cd discovery-server\discovery-server
mvn spring-boot:run
```

```powershell
cd auth-service\auth-service
mvn spring-boot:run
```

```powershell
cd user-service\user-service
mvn spring-boot:run
```

```powershell
cd measurement-service\measurement-service
mvn spring-boot:run
```

```powershell
cd api-gateway\api-gateway
mvn spring-boot:run
```

## Useful URLs

- Eureka Dashboard: `http://localhost:8761`
- Gateway Base: `http://localhost:8080`
- Gateway Health: `http://localhost:8080/actuator/health`

### Swagger

Currently enabled in:

- Measurement Service Swagger UI: `http://localhost:8083/swagger-ui/index.html`
- Measurement Service OpenAPI JSON: `http://localhost:8083/v3/api-docs`

If you want Swagger for `auth-service` and `user-service`, add Springdoc dependency there too.

## Gateway Routes

Configured routes in gateway:

- `/auth/**` -> `AUTH-SERVICE`
- `/users/**` -> `USER-SERVICE`
- `/measurements/**` -> `MEASUREMENT-SERVICE`
- `/api/measurements/**` -> `MEASUREMENT-SERVICE` (with `StripPrefix=1`)

## Notes

- Services use in-memory H2 databases (data resets on restart).
- Eureka is used for service registration/discovery.
- Keep JWT secret consistent across services that validate tokens.

## Docker Deploy

1. Create env file from template:

```powershell
copy docker.env.example .env
```

2. For deployment, use production template instead:

```powershell
copy docker.env.production.example .env
```

3. Update `.env` values (`APP_JWT_SECRET` must be strong; set real Google OAuth values for login).

4. Build and run:

```powershell
docker compose up -d --build
```

5. Check status:

```powershell
docker compose ps
```
