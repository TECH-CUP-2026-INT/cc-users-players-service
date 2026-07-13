package co.edu.escuelaing.techcup.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic API response wrapper for simple success/error messages.
 */
@Data
@AllArgsConstructor
public class ApiResponse {
    private String  message;
    private boolean success;
}