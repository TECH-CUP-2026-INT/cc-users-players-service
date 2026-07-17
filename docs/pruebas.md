# Testing

## How to run the tests

```bash
# Full suite
./mvnw test

# Full suite + coverage gate (JaCoCo >= 80%)
./mvnw verify
```

## What the tests cover

| Area | Covers |
|---|---|
| `application/service/*Test` | Business logic for the 5 registration/creation flows, profile view/edit, sports profile update, role update, disable-user (including the active-tournament block and the rollback-on-sync-failure path), OTP verification, and captaincy/existence checks |
| `infrastructure/adapter/in/rest/*ControllerTest` | HTTP contract of `UsuarioController`, `OTPController`, and `InternalPlayerController` |
| `infrastructure/adapter/out/identity/IdentityCredentialsAdapterTest` | Feign adapter behavior toward Identity Service, including failure mapping to `IdentityIntegrationException` |
| `infrastructure/adapter/out/teams/TeamRosterAdapterTest` | Feign adapter behavior toward Teams Service |
| `infrastructure/adapter/out/repository/*Test` | MongoDB repository adapters |
| `infrastructure/config/GlobalExceptionHandlerTest` | Status codes and response shape for each domain exception |
| `infrastructure/config/security/IdentityAuthenticationFilterTest`, `SecurityConfigIntegrationTest` | JWT delegation to Identity and route-level authorization rules |
| `core/util/UuidParserTest` | UUID parsing edge cases |

!!! note "What the test suite does not catch"
    The two integration issues described in
    [Architecture](arquitectura.md#a-broken-contract-role-and-status-sync)
    were found by reading the code of both services side by side, not by a
    failing test here — `IdentityCredentialsAdapterTest` and similar tests
    mock the Feign client rather than exercising it against a real (or
    contract-tested) Identity Service, so a drift in the actual HTTP
    contract between the two repos doesn't fail CI in either one.

## Minimum coverage

`pom.xml` includes `jacoco-maven-plugin` with a minimum line-coverage rule
of **80%**, enforced in the `verify` phase, excluding DTOs and the
application entry point. If coverage falls below the threshold, the build
fails.

## Tests in the CI pipeline

`.github/workflows/ci.yml` runs on every push to `feat/**`, `develop`, and
`main`, and on every PR into `develop`:

1. Starts MongoDB as a service container.
2. Sets up JDK 21 with Maven cache.
3. Runs `mvn clean verify -B` — tests plus the JaCoCo coverage gate — with
   `IDENTITY_SERVICE_URL` pointed at `localhost:8081` for the test run.
4. Uploads Surefire and JaCoCo reports as artifacts.
5. A separate `sonar` job (depends on `build`) runs a SonarCloud analysis
   using the JaCoCo report, skipped implicitly on PRs from forks that lack
   the `SONAR_TOKEN` secret.

Only if `build` passes does the pipeline move on to `dockerize-publish` and
`deploy` — see [Appendices](anexos.md#cicd-pipeline) for the full
breakdown, including why `dockerize-publish` currently can't succeed.
