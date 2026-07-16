package co.edu.escuelaing.techcup.users.core.util;

import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;

import java.util.UUID;

/**
 * Convierte identificadores recibidos como {@link String} (path variables, DTOs)
 * a {@link UUID}, lanzando un {@link BadRequestException} con un mensaje en
 * español si el formato no es válido.
 */
public final class UuidParser {

    private UuidParser() {
    }

    public static UUID parse(String value, String nombreCampo) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("El campo '" + nombreCampo + "' no tiene un formato UUID válido");
        }
    }
}
