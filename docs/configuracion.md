# Configuration

## Clone the repository

```bash
git clone https://github.com/TECH-CUP-2026-INT/cc-users-players-service.git
cd cc-users-players-service
```

## Run the service locally

Requires Java 21, Maven (or the included wrapper), and a reachable MongoDB
instance. There is no `docker-compose.yml` in this repo, so start MongoDB
manually:

```bash
docker run -d -p 27017:27017 --name techcup-mongo mongo:7
./mvnw spring-boot:run
```

The service is available at `http://localhost:5621/api/v1` (note the
`/api/v1` context path — it's set globally in `application.yml`, not
prefixed per-controller).

## Run with standalone Docker

!!! danger "No Dockerfile in this repository"
    Unlike the other TechCup services, there is currently no `Dockerfile`
    at the repository root. `.github/workflows/ci.yml` has a
    `dockerize-publish` job that runs `docker/build-push-action` expecting
    one to exist — that job will fail as soon as it runs on `main` until a
    `Dockerfile` is added. See [Appendices](anexos.md#known-issues).

## Environment variables

| Variable | Default value | Use |
|---|---|---|
| `SERVER_PORT` | `5621` | HTTP port the service listens on |
| `MONGODB_URI` | `mongodb://localhost:27017/techcup` | MongoDB connection string |
| `MAIL_HOST` | `smtp.gmail.com` | SMTP host, for OTP and temporary-password emails |
| `MAIL_PORT` | `587` | SMTP port |
| `MAIL_USERNAME` | _(empty)_ | SMTP user — required to actually send OTP/credential emails |
| `MAIL_PASSWORD` | _(empty)_ | SMTP password / app password — required |
| `IDENTITY_SERVICE_URL` | `http://localhost:5620` | Base URL of Identity Service, used both to provision credentials and to validate JWTs remotely |
| `TEAMS_SERVICE_URL` | `http://localhost:5622` | Base URL of Teams Service, used to check roster membership and active-tournament status |

These variables are resolved in `src/main/resources/application.yml`.

## Additional configuration in `application.yml`

| Property | Default value | Description |
|---|---|---|
| `app.security.bcrypt-strength` | `10` | BCrypt work factor used when hashing passwords before sending them to Identity Service |
| `app.otp.expiration-minutes` | `15` | Minutes before a registration OTP expires |
| `app.otp.max-attempts` | `3` | Attempts allowed per OTP |
| `app.allowed-domains` | `@mail.escuelaing.edu.co` | Institutional email domain(s) allowed for self-registration |

!!! danger "Production"
    `MAIL_USERNAME`, `MAIL_PASSWORD`, `IDENTITY_SERVICE_URL`, and
    `TEAMS_SERVICE_URL` all default to local/empty values suitable only for
    development. In any deployed environment they must be overridden — see
    [Requirements](requerimientos.md) for two integration issues that also
    need to be resolved independently of these variables being set
    correctly.

## Documentation (MkDocs)

This service's technical documentation is built with
[MkDocs](https://www.mkdocs.org/) and the
[Material for MkDocs](https://squidfunk.github.io/mkdocs-material/) theme.

### Installation

```bash
python -m venv .venv
source .venv/bin/activate   # Windows: .venv\Scripts\Activate.ps1
pip install -r requirements.txt
```

### Serve the documentation locally

```bash
mkdocs serve
```

Starts a local server at
[http://127.0.0.1:8000](http://127.0.0.1:8000) with live reload.

### Build the static site

```bash
mkdocs build
```

Generates the site into `site/` (git-ignored) — published automatically to
GitHub Pages by `.github/workflows/deploy-mkdocs.yml`, see
[Appendices](anexos.md#cicd-pipeline).

### Documentation structure

```
project/
│
├── docs/
│   ├── index.md
│   ├── introduccion.md
│   ├── requerimientos.md
│   ├── configuracion.md
│   ├── arquitectura.md
│   ├── api.md
│   ├── pruebas.md
│   ├── equipo.md
│   ├── anexos.md
│   └── assets/
│       └── stylesheets/
│           └── extra.css
│
├── mkdocs.yml
├── src/
```

Theme colors and typography are defined in
`docs/assets/stylesheets/extra.css` and declared in `mkdocs.yml` under
`extra_css`.
