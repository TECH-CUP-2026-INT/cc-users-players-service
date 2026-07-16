package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto;

import co.edu.escuelaing.techcup.users.core.domain.enums.AccountStatus;

public record UpdateStatusRequestDTO(AccountStatus status) {
}
