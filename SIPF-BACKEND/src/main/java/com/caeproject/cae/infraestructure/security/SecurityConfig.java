package com.caeproject.cae.infraestructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(java.util.List.of("*"));
                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        // Permite acceso publico al Login y endpoints asociados a los iniciales con api/auth/
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        .requestMatchers("/h2-console/**").permitAll() //MOMENTANEO H2
                        .requestMatchers("/api/programas/**").hasRole("COORDINADOR")
                        .requestMatchers("/api/fichas/**").hasAnyRole("COORDINADOR", "ADMINISTRADOR")
                        .requestMatchers("/api/trimestres/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/especialidades/**").hasAnyRole("ADMINISTRADOR", "INSTRUCTOR")
                        .requestMatchers("/api/instructor-especialidad/**").hasAnyRole("ADMINISTRADOR", "INSTRUCTOR")
                        .requestMatchers("/api/competencia-especialidad/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/excel/alimentacion").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/raps/**").hasAnyRole("COORDINADOR", "ADMINISTRADOR")
                        .requestMatchers("/api/competencias/**").hasAnyRole("COORDINADOR", "ADMINISTRADOR")
                        .requestMatchers("/api/disponibilidadinstructor", "/api/disponibilidadinstructor/**").hasAnyRole("INSTRUCTOR", "ADMINISTRADOR")
                        .requestMatchers("/api/ubicaciones").hasAnyRole("ADMINISTRADOR", "INSTRUCTOR","COORDINADOR" )
                        .requestMatchers("/api/programacionacademica", "/api/programacionacademica/**").hasAnyRole("COORDINADOR", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMINISTRADOR")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }

    // 2. Codificación de contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}