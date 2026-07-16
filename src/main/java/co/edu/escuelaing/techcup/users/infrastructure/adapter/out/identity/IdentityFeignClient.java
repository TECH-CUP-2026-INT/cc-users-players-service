package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity;

import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.CreateCredentialsRequestDTO;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.TokenValidationResponseDTO;
import co.edu.escuelaing.techcup.users.infrastructure.adapter.out.identity.dto.UpdateRoleRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "identity-service", url = "${identity.service.base-url}")
public interface IdentityFeignClient {

    @PostMapping("/api/v1/internal/credentials")
    void crearCredenciales(@RequestBody CreateCredentialsRequestDTO request);

    @PutMapping("/api/v1/internal/credentials/{userId}/role")
    void actualizarRol(@PathVariable("userId") String userId, @RequestBody UpdateRoleRequestDTO request);

    @PostMapping("/api/v1/token/validate")
    TokenValidationResponseDTO validar(@RequestHeader("Authorization") String bearerToken);
}
