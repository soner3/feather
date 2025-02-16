package com.feather.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    protected SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/feather/profile/v1/profile/public").permitAll()
                        .pathMatchers("/feather/authserver/**").permitAll()
                        .pathMatchers("/feather/**").authenticated()
                        .pathMatchers("/**").permitAll()
                        .anyExchange().authenticated())
                .csrf(csrfConfig -> csrfConfig.disable())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

}
