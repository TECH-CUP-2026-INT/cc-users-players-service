package co.edu.escuelaing.techcup.users.infrastructure.config.security;

import co.edu.escuelaing.techcup.users.core.ports.out.IdentityTokenValidationPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class IdentityAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final IdentityTokenValidationPort identityTokenValidationPort;

    public IdentityAuthenticationFilter(IdentityTokenValidationPort identityTokenValidationPort) {
        this.identityTokenValidationPort = identityTokenValidationPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            Optional<IdentityTokenValidationPort.TokenInfo> tokenInfo =
                    identityTokenValidationPort.validar(authHeader);

            tokenInfo.ifPresent(info -> {
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + info.role()));
                var authentication = new UsernamePasswordAuthenticationToken(
                        info.userId(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        }

        filterChain.doFilter(request, response);
    }
}
