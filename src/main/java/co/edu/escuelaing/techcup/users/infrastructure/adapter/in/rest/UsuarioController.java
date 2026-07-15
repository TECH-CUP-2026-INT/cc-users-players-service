package co.edu.escuelaing.techcup.users.infrastructure.adapter.in.rest;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.ports.in.RegistroEstudianteUseCase;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroEstudianteRequest;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.in.dto.RegistroResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final RegistroEstudianteUseCase registroEstudianteUseCase;

    public UsuarioController(RegistroEstudianteUseCase registroEstudianteUseCase) {
        this.registroEstudianteUseCase = registroEstudianteUseCase;
    }

    @PostMapping("/registro/estudiante")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroResponse registrarEstudiante(@Valid @RequestBody RegistroEstudianteRequest request) {
        Usuario usuario = registroEstudianteUseCase.registrarEstudiante(
            request.getNombreCompleto(),
            request.getCorreoInstitucional(),
            request.getContrasena(),
            request.getProgramaAcademico(),
            request.getSemestre(),
            request.getTipoIdentificacion(),
            request.getNumeroIdentificacion()
        );

        return new RegistroResponse(
            usuario.getUsuarioId(),
            usuario.getEstado(),
            usuario.getRol(),
            "Usuario registrado exitosamente. Se ha enviado un código OTP a tu correo."
        );
    }
}
