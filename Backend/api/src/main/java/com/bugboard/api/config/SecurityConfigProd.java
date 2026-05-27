package com.bugboard.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("prod")
public class SecurityConfigProd {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // pubbliche
                        .requestMatchers("/", "/error").permitAll()

                        // solo con token
                        .requestMatchers("/api/**").authenticated()

                        // solo per la fase di sviluppo, per permettere la creazione di utenti senza autenticazione
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users").permitAll()

                        // tutto il restyo
                        .anyRequest().permitAll()
                )

                // abilita validazione JWT (Cognito)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
