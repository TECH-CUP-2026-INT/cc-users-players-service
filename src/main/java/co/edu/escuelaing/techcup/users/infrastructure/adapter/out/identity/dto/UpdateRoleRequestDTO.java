package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto;

import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;

public record UpdateRoleRequestDTO(UserRole role) {
}
