# API

## Interactive documentation (Swagger UI)

With the service running:
[http://localhost:5621/api/v1/swagger-ui.html](http://localhost:5621/api/v1/swagger-ui.html)

The raw OpenAPI specification is available at `/api/v1/v3/api-docs`.

## Authentication

This service does not verify a JWT's signature itself — it forwards the
`Authorization` header to Identity Service's `POST /api/v1/token/validate`
on every request and trusts the response. See
[Architecture](arquitectura.md#security) for the detail. Endpoints under
`/usuarios/admin/**` additionally require the `ADMIN` role.

## Endpoints

All paths below are relative to the `/api/v1` context path (e.g. the full
path for registration is `/api/v1/usuarios/registro/estudiante`).

### Registration (public)

| Method | Path | Description |
|---|---|---|
| `POST` | `/usuarios/registro/estudiante` | Register a student (institutional email, `app.allowed-domains`) |
| `POST` | `/usuarios/registro/invitado` | Register a guest |
| `POST` | `/usuarios/registro/egresado` | Register a graduate |

Each of these creates the profile here **and** provisions matching login
credentials in Identity Service in the same flow, then sends an OTP by
email for identity verification.

### OTP (public)

| Method | Path | Description |
|---|---|---|
| `POST` | `/otp/verificar` | Verify the OTP code sent at registration |

### Profile (authenticated)

| Method | Path | Description |
|---|---|---|
| `GET` | `/usuarios/perfil` | View the authenticated user's own profile |
| `PUT` | `/usuarios/perfil` | Edit the authenticated user's own profile (name, academic program, semester) |
| `GET` | `/usuarios/{userId}/perfil` | View another user's public profile |
| `PUT` | `/usuarios/perfil/deportivo` (multipart) | Update the authenticated user's sports profile (position, jersey number, optional photo) |

### Admin (requires `ADMIN` role)

| Method | Path | Description |
|---|---|---|
| `POST` | `/usuarios/admin/arbitros` | Create a referee account with a temporary password |
| `POST` | `/usuarios/admin/administradores` | Create an admin/organizer account with a temporary password |
| `PUT` | `/usuarios/admin/{userId}/rol` | Update a user's role — **currently fails with `502`**, see [Requirements](requerimientos.md) |
| `PUT` | `/usuarios/admin/{userId}/deshabilitar` | Disable a user account — **currently fails with `502`**, see [Requirements](requerimientos.md) |

### Service-to-service (`/internal/**`, unauthenticated)

| Method | Path | Consumed by | Description |
|---|---|---|---|
| `GET` | `/internal/players/{playerId}/exists` | Teams Service | Whether a player with this ID exists |
| `GET` | `/internal/players/{playerId}/profile` | Identity Service, Teams Service | Public profile — **missing `estado` field**, see [Architecture](arquitectura.md#a-broken-contract-role-and-status-sync) |
| `GET` | `/internal/players/{playerId}/captaincy` | Teams Service | Whether the player is currently a captain |

## Example: register a student

`POST /api/v1/usuarios/registro/estudiante`

```json
{
  "nombreCompleto": "Ada Lovelace",
  "correoInstitucional": "ada.lovelace@mail.escuelaing.edu.co",
  "contrasena": "a-strong-password",
  "programaAcademico": "Ingeniería de Sistemas",
  "semestre": 6,
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "1000123456"
}
```

Response `201 Created`:

```json
{
  "id": "1f2e3d4c-5b6a-4978-8a9b-0c1d2e3f4a5b",
  "estado": "ACTIVE",
  "rol": "PLAYER",
  "mensaje": "Usuario registrado exitosamente. Se ha enviado un código OTP a tu correo."
}
```

If Identity Service rejects the credentials-creation call (e.g. the email
already exists there), the whole registration fails with `502`
(`IdentityIntegrationException`) rather than leaving an orphaned profile
with no way to log in.

## Example: get another player's public profile

`GET /api/v1/usuarios/{userId}/perfil`

```json
{
  "id": "1f2e3d4c-5b6a-4978-8a9b-0c1d2e3f4a5b",
  "nombreCompleto": "Ada Lovelace",
  "tipoUsuario": "STUDENT",
  "rol": "PLAYER",
  "programaAcademico": "Ingeniería de Sistemas"
}
```

This is the same shape returned by the internal
`GET /internal/players/{playerId}/profile` used by Identity and Teams —
see the note in [Architecture](arquitectura.md#a-broken-contract-role-and-status-sync)
about the missing `estado` field.

## Errors

Business errors are handled centrally in `GlobalExceptionHandler`
(`@RestControllerAdvice`) and returned as `{"error": "..."}`, with an
appropriate HTTP status:

| Code | Cause |
|---|---|
| `400` | Payload validation (`MethodArgumentNotValidException`, `BadRequestException`) — validation errors additionally include a `detalles` map of field → message |
| `401` | Not authenticated (custom JSON body via `authenticationEntryPoint`) |
| `403` | Authenticated but missing the required role (custom JSON body via `accessDeniedHandler`) |
| `404` | Resource not found (`NotFoundException`) |
| `409` | Conflict (`ConflictException`) — e.g. disabling an already-disabled user |
| `502` | An external service failed (`IdentityIntegrationException`, `EmailSenderException`, `TeamsIntegrationException`) |
