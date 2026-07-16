package co.edu.escuelaing.techcup.users.infrastructure.config.security;

import co.edu.escuelaing.techcup.users.core.ports.out.IdentityTokenValidationPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdentityAuthenticationFilterTest {

    @Mock
    private IdentityTokenValidationPort identityTokenValidationPort;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private IdentityAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new IdentityAuthenticationFilter(identityTokenValidationPort);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void noAutenticaSiNoHayHeaderAuthorization() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void noAutenticaSiIdentityRechazaElToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalido");
        when(identityTokenValidationPort.validar("Bearer invalido")).thenReturn(Optional.empty());

        filter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void autenticaConRolPlayerParaUnTokenDeJugador() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valido-player");
        when(identityTokenValidationPort.validar("Bearer valido-player"))
                .thenReturn(Optional.of(new IdentityTokenValidationPort.TokenInfo("u1", "jugador@techcup.com", "PLAYER")));

        filter.doFilter(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo("u1");
        assertThat(authentication.getAuthorities())
                .extracting(Object::toString)
                .containsExactly("ROLE_PLAYER");
    }

    @Test
    void autenticaConRolAdminParaUnTokenDeAdmin() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valido-admin");
        when(identityTokenValidationPort.validar("Bearer valido-admin"))
                .thenReturn(Optional.of(new IdentityTokenValidationPort.TokenInfo("u1", "admin@techcup.com", "ADMIN")));

        filter.doFilter(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getAuthorities())
                .extracting(Object::toString)
                .containsExactly("ROLE_ADMIN");
        verify(filterChain).doFilter(request, response);
    }
}
