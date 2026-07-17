# Users & Players Service (service-users)

Spring Boot microservice that is the **source of truth for user identity
and profile data** on the university tournament **TechCup Fútbol**:
registration (students, guests, graduates), profile management, sports
profile (position, jersey number), and account/role administration. It
delegates authentication credentials to Identity Service and is, in turn,
queried live by Identity and Teams for role/status and roster data.

[View on GitHub](https://github.com/TECH-CUP-2026-INT/cc-users-players-service){ .md-button .md-button--primary }
[Explore the API](api.md){ .md-button }

## Documentation map

| Section | Content |
|---|---|
| [Introduction](introduccion.md) | Context, purpose, and scope of the service |
| [Requirements](requerimientos.md) | Functional and non-functional requirements, technical prerequisites |
| [Configuration](configuracion.md) | Environment variables, running locally, and Docker deployment |
| [Architecture](arquitectura.md) | Layers, data model, and integrations with other services |
| [API](api.md) | REST endpoints, authentication, and Swagger UI |
| [Testing](pruebas.md) | Test strategy and how to run it |
| [Team](equipo.md) | TECH-CUP 2026 INT team members and roles |
| [Appendices](anexos.md) | Glossary, security notes, and references |

## Quick summary

| Layer | Technology |
|---|---|
| Language / runtime | Java 21 |
| Framework | Spring Boot 3.5.6 |
| Build | Maven |
| Persistence | MongoDB |
| Service-to-service | OpenFeign (Identity, Teams) |
| API | Spring Web (REST, multipart) + springdoc-openapi |
| Security | JWT validated remotely via Identity Service (no local signing) |
| CI/CD | GitHub Actions (build, test, SonarCloud, Docker image, deploy to Azure) |
| Documentation | MkDocs + Material for MkDocs |

## Quick start

```bash
docker run -d -p 27017:27017 --name techcup-mongo mongo:7
./mvnw spring-boot:run
```

The service listens on port `5621` by default, under the context path
`/api/v1` — see [Configuration](configuracion.md).

!!! warning "No Dockerfile in this repository"
    Unlike the other TechCup services, this repository has no `Dockerfile`
    at the root, even though `.github/workflows/ci.yml` has a
    `dockerize-publish` job that runs `docker/build-push-action`. That job
    will fail on the next push to `main` until a `Dockerfile` is added —
    see [Appendices](anexos.md#known-issues).

## Where to go next

- [Architecture](arquitectura.md) for the hexagonal structure and how role/status live queries work.
- [API](api.md) for the full endpoint reference.
- [Requirements](requerimientos.md) for known gaps against the platform's requirement sheet, including two cross-service integration issues.
