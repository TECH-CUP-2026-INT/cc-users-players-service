# Introduction

## Context

**TechCup Fútbol** is a university tournament whose digital platform is
made up of a set of independent microservices, each owning a bounded
business domain (identity, users/players, teams, tournaments, logistics,
and so on).

The **Users & Players Service** (`service-users`) is the microservice that
owns everything about a person as a platform user: how they register,
what their profile looks like, what role and account status they hold, and
their sports profile as a player (position, jersey number, photo). It is
explicitly documented in its own code as **"the source of truth for
identity, for all microservices"** — Identity Service only stores login
credentials that reference a `Usuario.id` minted here.

## Purpose

Give the platform a single, authoritative place to manage user identity, so
that:

- Every kind of end user (student, guest, graduate, referee, admin,
  organizer) has one consistent registration and profile model.
- Role and account status are owned here, not duplicated and drifted across
  services — Identity Service is expected to query this service live
  instead of keeping its own synced copy.
- Other services (Teams, Identity) can cheaply check whether a player
  exists and fetch their public profile without needing direct database
  access.
- Registering a new user also provisions their login credentials in
  Identity Service, in the same flow, so a user never ends up "half
  created."

## Actors

| Actor | Capabilities |
|---|---|
| **Student / Guest / Graduate** (self-registration) | Registers themselves, verifies their OTP, views/edits their own profile, updates their sports profile |
| **Admin** | Creates referee and admin/organizer accounts, updates any user's role, disables a user account |
| **Identity Service** | Calls `GET /internal/players/{userId}/profile` (unauthenticated, service-to-service) to resolve a user's live role and status |
| **Teams Service** | Calls the same profile endpoint plus `GET /internal/players/{playerId}/exists` and `.../captaincy` (all unauthenticated, service-to-service) |

Authentication for end-user endpoints is delegated to **Identity Service**:
this service validates every JWT by calling Identity's
`POST /api/v1/token/validate` remotely, the same pattern used by Teams
Service. See [Architecture](arquitectura.md#security).

## Scope

### What this service DOES do

1. Register students, guests, and graduates (self-service, with OTP
   verification sent by email).
2. Let an Admin create referee and admin/organizer accounts with a
   temporary password.
3. Let a user view and edit their own profile, and view another player's
   public profile.
4. Let a user update their sports profile (position, jersey number,
   optional photo).
5. Let an Admin update a user's role or disable their account.
6. Expose unauthenticated, service-to-service endpoints for Identity and
   Teams to resolve player existence, public profile, and captaincy status.
7. Provision matching credentials in Identity Service whenever a user is
   registered or created.

### What it does NOT do (owned by other services)

| Responsibility | Owning service |
|---|---|
| Login, OTP-based two-factor authentication, JWT issuance/validation | Identity Service |
| Team membership, invitations, captaincy transfer | Teams Service |
| Tournament definitions and enrollment | mk-tournament-service |
| Whether a player currently has an active tournament (indirectly, via Teams) | mk-tournament-service, proxied through Teams |

See [Architecture](arquitectura.md) for how this service talks to each of
them — including two integration mismatches found while writing this
documentation, detailed in [Requirements](requerimientos.md) and
[Appendices](anexos.md#known-issues).
