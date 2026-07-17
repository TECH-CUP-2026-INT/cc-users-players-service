# Appendices

## Glossary

| Term | Meaning |
|---|---|
| Usuario | The central entity of this service — a platform user's identity, profile, role, and status; the source of truth Identity Service defers to |
| Sports profile | A player's position, jersey number, and photo (`PUT /usuarios/perfil/deportivo`) |
| OTP | One-time code sent by email at registration, used to verify the user's identity before their account is fully active |
| Port (architecture) | A domain interface that abstracts an external integration |
| Fails open | A check that defaults to a permissive result when the service it depends on is unreachable, rather than blocking the caller |

## Known issues

This section exists specifically to track the problems found while writing
this documentation, so they don't get lost between now and whenever
someone picks them up.

### 1. Role and status sync to Identity Service is broken

**Impact:** every admin action to change a user's role
(`PUT /usuarios/admin/{userId}/rol`) or disable an account
(`PUT /usuarios/admin/{userId}/deshabilitar`) currently fails with `502`.

**Cause:** `IdentityFeignClient` calls
`PUT /api/v1/internal/credentials/{userId}/role` and `.../status` on
Identity Service. Those endpoints don't exist on Identity's current `main`
branch — see `InternalCredentialController` there, which only exposes
`POST /internal/credentials`, `GET .../email`, and
`POST .../revoke-sessions`.

**Possible directions** (needs a decision made jointly with whoever owns
`cc-identity-service`, not unilaterally here):

- Restore the two `PUT` endpoints on Identity, reverting to a push-based
  sync model, or
- Remove the push calls from this service entirely and rely purely on
  Identity's live-query model (`GET /internal/players/{userId}/profile`) —
  which requires fixing issue #2 below first, since that's the endpoint
  Identity actually queries.

### 2. The live-query profile endpoint is missing the `estado` field

**Impact:** potentially breaks **every login and OTP validation** in
Identity Service, not just admin operations here — this is the more urgent
of the two issues.

**Cause:** `PerfilPublicoResponse` (returned by
`GET /internal/players/{userId}/profile`) has no `estado` field. Identity's
`UsersPlayersProfileAdapter` reads a `PlayerProfileResponse(String rol,
String estado)` record from this same response and calls
`AccountStatus.valueOf(response.estado())`. A missing field deserializes as
`null`, and `Enum.valueOf(_, null)` throws `NullPointerException` — which
Identity's adapter doesn't catch (it only catches `FeignException` and
`IllegalArgumentException`).

**Fix:** add an `estado` field (`usuario.getEstado().name()`) to
`PerfilPublicoResponse`, or introduce a separate, explicit internal DTO for
this endpoint that includes it. Low-risk, single-file change here — but
worth confirming Identity's exact expected field name/casing before
shipping it, since that contract lives in the other repo.

### 3. No `Dockerfile` in this repository

**Impact:** the `dockerize-publish` job in `.github/workflows/ci.yml` runs
`docker/build-push-action` with `context: .`, which requires a `Dockerfile`
at the repository root. Without one, that job fails on the next push to
`main`, and `deploy` (which needs it) never runs.

**Fix:** add a multi-stage `Dockerfile` following the same pattern used by
`cc-identity-service` and `cc-teams-service` (`eclipse-temurin:21-jdk-jammy`
build stage, `eclipse-temurin:21-jre-jammy` runtime stage, `EXPOSE 5621`).

## CI/CD pipeline

Defined in `.github/workflows/ci.yml`. Four jobs:

1. **`build`** — `mvn clean verify -B` against an ephemeral MongoDB service
   container. Runs on every push to `feat/**`, `develop`, `main`, and every
   PR into `develop`. Uploads Surefire and JaCoCo reports as artifacts.
2. **`sonar`** — depends on `build`. Downloads the JaCoCo report and runs a
   SonarCloud analysis (`SonarSource/sonarqube-scan-action`) against
   project key `TECH-CUP-2026-INT_cc-users-players-service`, using the
   `SONAR_TOKEN` secret.
3. **`dockerize-publish`** — depends on `build`, only on `main`. Builds the
   JAR and attempts to build/push a Docker image to
   `ghcr.io/TECH-CUP-2026-INT/cc-users-players-service`. **Currently fails**
   — see [Known issues](#3-no-dockerfile-in-this-repository).
4. **`deploy`** — depends on `dockerize-publish`, only on `main`. Logs into
   Azure via `azure/login@v2` using the `AZURE_CREDENTIALS` secret (a
   service principal, not a publish profile — same pattern as
   `cc-teams-service`), then runs `az containerapp update` to point the
   Azure Container App `users-players-service` (resource group `techcup`)
   at the freshly pushed `:latest` image. Never runs today, since
   `dockerize-publish` fails first.

A separate workflow, `.github/workflows/deploy-mkdocs.yml`, builds this
documentation site and publishes it to GitHub Pages on every push to
`main`. It requires this repository's **Settings → Pages → Build and
deployment → Source** to be set to **"GitHub Actions"** — a one-time manual
toggle, otherwise the workflow runs but nothing gets published.

## References

- [MkDocs](https://www.mkdocs.org/) — static documentation site generator
  used by this project.
- [Material for MkDocs](https://squidfunk.github.io/mkdocs-material/) —
  theme used for the site.
- [Spring Boot](https://spring.io/projects/spring-boot) — the service's
  framework.
- [springdoc-openapi](https://springdoc.org/) — OpenAPI specification and
  Swagger UI generation.
- [JaCoCo](https://www.jacoco.org/jacoco/) — test coverage.
- [OpenFeign](https://spring.io/projects/spring-cloud-openfeign) —
  declarative HTTP clients used for the Identity and Teams integrations.
- [SonarCloud](https://sonarcloud.io/) — static analysis, run in CI.

## Changelog

| Date | Change |
|---|---|
| 2026-07-17 | Documentation restructured to match the `am-logistic-service` pattern (Home / Introduction / Requirements / Configuration / Architecture / API / Testing / Team / Appendices). Content verified against the actual controllers, use cases, and adapters. Identified and documented three known issues: broken role/status sync with Identity Service, a missing `estado` field on the profile endpoint Identity depends on for live login/OTP checks, and a missing `Dockerfile` that breaks the CI/CD pipeline's Docker build stage. |
