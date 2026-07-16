package co.edu.escuelaing.techcup.users.core.exception;

/**
 * Se lanza cuando falla la comunicación con Teams Service (por ejemplo, al
 * consultar el roster del equipo de un jugador para validar unicidad de
 * número de camiseta).
 */
public class TeamsIntegrationException extends RuntimeException {
    public TeamsIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
