package com.caeproject.cae.infraestructure.security;

import com.caeproject.cae.domain.ports.exceptions.sessionexceptions.SesionCerradaException;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final UsuarioRepository usuarioRepository;

    public JwtFilter(
            JwtUtil jwtUtil,
            HandlerExceptionResolver handlerExceptionResolver,
            UsuarioRepository usuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtUtil.extractUsername(jwt);
            final String role = jwtUtil.extractClaim(jwt, claims -> claims.get("role", String.class));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                // Bloque de validación de sesión
                if (jwtUtil.isTokenValid(jwt, userEmail)) {
                    final String sessionJwt = jwtUtil.extractClaim(jwt, claims -> claims.get("sessionId", String.class));

                    Usuario usuario = usuarioRepository.findByCorreo(userEmail)
                            .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

                    //no tocar, es el comparador de la sesion nueva y antigua
                    if (usuario.getTokenSession() == null || !usuario.getTokenSession().equals(sessionJwt)) {
                        throw new SesionCerradaException();
                    }
                }

                // Asignamos autoridades SOLO si el usuario fue validado correctamente
                List<GrantedAuthority> authorities = role != null
                        ? List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        : List.of();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, null, authorities
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            exception.printStackTrace();
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
