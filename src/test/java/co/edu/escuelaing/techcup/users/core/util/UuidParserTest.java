package co.edu.escuelaing.techcup.users.core.util;

import co.edu.escuelaing.techcup.users.core.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UuidParserTest {

    @Test
    void parseaUnUuidValido() {
        UUID id = UUID.randomUUID();

        assertThat(UuidParser.parse(id.toString(), "usuarioId")).isEqualTo(id);
    }

    @Test
    void lanzaBadRequestConMensajeEspecificoSiElFormatoEsInvalido() {
        assertThatThrownBy(() -> UuidParser.parse("no-es-un-uuid", "usuarioId"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("El campo 'usuarioId' no tiene un formato UUID válido");
    }
}
