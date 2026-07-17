# Team

This service is developed and maintained by the **TECH-CUP 2026 INT**
team, as part of the TechCup Fútbol tournament platform.

## Members

| Name | Role |
|---|---|
| Carlos Duban Rojas Riveros | Desarrollador |
| Juan Eduardo Vera Acero | Desarrollador |
| José Luis García Chinchilla | Desarrollador |
| Willian Santiago Ruiz Medina | Desarrollador |

!!! note "Pending"
    This table is a template. Update it with each team member's name, role
    (e.g. Backend, QA, DevOps, Product Owner), and contact information
    before publishing the documentation.

## Repository

- **Organization:** [TECH-CUP-2026-INT](https://github.com/TECH-CUP-2026-INT)
- **Repository:** [cc-users-players-service](https://github.com/TECH-CUP-2026-INT/cc-users-players-service)
- **Related services:** `cc-identity-service` (authentication and credentials), `cc-teams-service` (team membership and captaincy)

## Contribution conventions

- Work branches: `feat/**` into `develop`; `develop`/`feat/**` into `main`
  via pull request.
- Every change goes through the CI pipeline (build, tests, coverage,
  SonarCloud analysis) before it can be merged — see
  [Configuration](configuracion.md).
- Documentation changes are edited directly under `docs/` and published via
  MkDocs (the "Edit this page" button on the site, enabled through
  `edit_uri` in `mkdocs.yml`).
- Given the cross-service contract issues documented in
  [Architecture](arquitectura.md#a-broken-contract-role-and-status-sync),
  any change to `InternalPlayerController`'s response shape or to
  `IdentityFeignClient`'s target paths should be coordinated with whoever
  owns `cc-identity-service`.
