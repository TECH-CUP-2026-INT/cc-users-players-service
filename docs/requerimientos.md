# Requirements

Functional requirements taken from the platform's test-case map (TC-01
through TC-05 and TC-13 through TC-19, Users & Players section) contrasted
against the current implementation.

## Functional requirements

| ID | Requirement | Status |
|---|---|---|
| TC-01 | Student registration | ✅ Implemented (`POST /usuarios/registro/estudiante`) — creates the profile here and provisions credentials in Identity Service |
| TC-02 | Guest registration | ✅ Implemented (`POST /usuarios/registro/invitado`) |
| TC-03 | Graduate registration | ✅ Implemented (`POST /usuarios/registro/egresado`) |
| TC-04 | Referee creation (by Admin) | ✅ Implemented (`POST /usuarios/admin/arbitros`), with a generated temporary password |
| TC-05 | Admin/organizer creation (by Admin) | ✅ Implemented (`POST /usuarios/admin/administradores`) |
| TC-13 | View own profile | ✅ Implemented (`GET /usuarios/perfil`) |
| TC-14 | Edit own profile | ✅ Implemented (`PUT /usuarios/perfil`) |
| TC-15 | View another player's profile | ✅ Implemented (`GET /usuarios/{userId}/perfil`) |
| TC-16 | View a player's tournament history | ❌ Not implemented — depends on Tournament Service, which doesn't expose the needed endpoint yet |
| TC-17 | View a player's tournament statistics | ⚠️ Reinterpreted, not implemented as originally scoped — the code that carries the `TC-17` label is actually the **sports profile** update (`PUT /usuarios/perfil/deportivo`: position, jersey number, photo), not tournament statistics. No statistics endpoint exists |
| TC-18 | Promote a player to captain | ❌ **Broken in practice** — `PUT /usuarios/admin/{userId}/rol` updates the role locally, then calls `PUT /api/v1/internal/credentials/{userId}/role` on Identity Service to sync it. That endpoint **no longer exists** on Identity's current `main` branch (removed in favor of a live-query model — see [Architecture](arquitectura.md#a-broken-contract-role-and-status-sync)). The Feign call gets a 404, the local role change is rolled back, and the request fails with `502` |
| TC-19 | Disable user account | ❌ **Broken in practice**, same root cause as TC-18 — `PUT /usuarios/admin/{userId}/deshabilitar` correctly checks for an active tournament first (via Teams → Tournament, failing open), but the subsequent call to sync `INACTIVE` status to Identity hits the same removed endpoint and the whole operation is rolled back with `502` |

!!! danger "Two confirmed cross-service integration bugs"
    These were found by comparing this service's outbound Feign calls
    against `cc-identity-service`'s actual current `main` branch — not
    from a test failure, so they're worth verifying independently before
    treating this note as settled:

    1. **TC-18/TC-19 role & status sync is dead code pointing at a removed
       endpoint.** `IdentityFeignClient.actualizarRol` and
       `.actualizarEstado` call `PUT /api/v1/internal/credentials/{userId}/role`
       and `.../status`. Identity's `InternalCredentialController` on
       `main` only exposes `POST /internal/credentials`,
       `GET /internal/credentials/{userId}/email`, and
       `POST /internal/credentials/{userId}/revoke-sessions` — no role or
       status endpoints. Every admin role change or account disable will
       fail with `502`.
    2. **The profile endpoint Identity depends on is missing the field it
       needs.** `GET /internal/players/{userId}/profile` here returns
       `PerfilPublicoResponse`, which has no `estado` (account status)
       field. Identity's `UsersPlayersProfileAdapter` expects a JSON
       `estado` field to build an `AccountStatus`; when it's absent, calling
       `AccountStatus.valueOf(null)` throws a `NullPointerException` that
       Identity's adapter does not catch (it only catches `FeignException`
       and `IllegalArgumentException`). Since Identity calls this endpoint
       on **every login and every OTP validation**, this can break
       authentication platform-wide, not just role/status administration.

    See [Appendices](anexos.md#known-issues) for suggested next steps.

## Non-functional requirements

| ID | Requirement |
|---|---|
| NFR-01 | **Authentication delegated to Identity**: every protected endpoint depends on Identity Service being reachable to validate a JWT. |
| NFR-02 | **Rollback on partial failure**: role and status changes are applied locally first; if syncing to Identity fails, the local change is rolled back rather than left inconsistent (see the bug above — the mechanism works, but the endpoint it targets doesn't exist). |
| NFR-03 | **Resilience against unavailable external services**: the active-tournament check (via Teams → Tournament) fails open, never blocking a legitimate disable request because of an unrelated outage. |
| NFR-04 | **Domain-restricted self-registration**: only institutional emails under `app.allowed-domains` (`@mail.escuelaing.edu.co`) can self-register. |
| NFR-05 | **Maintainability**: business logic is decoupled from REST controllers and external integration details behind ports/adapters (hexagonal architecture). |
| NFR-06 | **Test coverage**: JaCoCo line-coverage gate of 80%, enforced in the `verify` phase, excluding DTOs and the application entry point. |
| NFR-07 | **Network-level protection for service-to-service endpoints**: `/internal/**` carries no application-level authentication — it relies entirely on being unreachable from outside the trusted internal network. |

## Technical prerequisites

To develop and run the service locally:

| Tool | Minimum version | Use |
|---|---|---|
| [Java (JDK)](https://adoptium.net/) | 21 | Compiling and running the service |
| [Docker](https://www.docker.com/) | 24+ | Running a local MongoDB instance |
| [Git](https://git-scm.com/) | 2.x | Version control |
| Maven Wrapper (`mvnw`, included in the repo) | — | No local Maven install required |

To work on the documentation:

| Tool | Minimum version | Use |
|---|---|---|
| [Python](https://www.python.org/) | 3.9+ | Required by MkDocs |
| [MkDocs](https://www.mkdocs.org/) + [Material for MkDocs](https://squidfunk.github.io/mkdocs-material/) | — | Building the documentation site |

See [Configuration](configuracion.md) for installation steps for each tool
and the service's environment variables.
