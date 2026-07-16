package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto;

import co.edu.escuelaing.techcup.users.core.domain.enums.UserRole;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;

public record CreateCredentialsRequestDTO(
        String userId,
        String email,
        String password,
        String fullName,
        UserType userType,
        UserRole role
) {
}
