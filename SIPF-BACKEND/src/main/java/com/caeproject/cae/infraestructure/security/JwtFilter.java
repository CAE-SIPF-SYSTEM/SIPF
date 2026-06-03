package com.caeproject.cae.infraestructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
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


    public JwtFilter(
            JwtUtil jwtUtil,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtUtil = jwtUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        //validar el tipo de token y si llega dentro de la peticion
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            final String jwt = authHeader.substring(7); //7 caracteres
            final String userEmail = jwtUtil.extractUsername(jwt);
            final String role = jwtUtil.extractClaim(jwt, claims -> claims.get("role", String.class)); // Extraer el rol del token

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // validar si el correo existe en el token y no hay sesión en el contexto
            if (userEmail != null && authentication == null) {

                // Validacion de token expirado o firma invalida
                if (jwtUtil.isTokenValid(jwt, userEmail)) {
                    
                    // Asignamos la autoridad (rol) obtenida del token
                    List<GrantedAuthority> authorities = role != null 
                            ? List.of(new SimpleGrantedAuthority("ROLE_" + role)) 
                            : List.of();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            authorities
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //autenticacion dentro de la seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            //peticion
            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            //excepcion de token por que falla
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}