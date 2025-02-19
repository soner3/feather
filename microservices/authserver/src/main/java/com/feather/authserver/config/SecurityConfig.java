package com.feather.authserver.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.feather.authserver.filter.TokenAuthenticationFilter;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final TokenAuthEntryPoint tokenAuthEntryPoint;

        // @Bean
        // @Order(1)
        // protected SecurityFilterChain
        // authorizationServerSecurityFilterChain(HttpSecurity http)
        // throws Exception {
        // OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
        // OAuth2AuthorizationServerConfigurer
        // .authorizationServer();

        // http
        // .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
        // .with(authorizationServerConfigurer, (authorizationServer) ->
        // authorizationServer
        // .oidc(Customizer.withDefaults()))
        // .authorizeHttpRequests((authorize) -> authorize
        // .anyRequest().authenticated())
        // .cors(Customizer.withDefaults())
        // .exceptionHandling((exceptions) -> exceptions
        // .defaultAuthenticationEntryPointFor(
        // new LoginUrlAuthenticationEntryPoint("/login"),
        // new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

        // return http.build();
        // }

        @Bean
        @Order(2)
        protected SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                        TokenAuthenticationFilter tokenAuthenticationFilter)
                        throws Exception {
                http
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/v1/user/public").permitAll()
                                                .requestMatchers("/v1/token/**").permitAll()
                                                .requestMatchers("/actuator/**").permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
                                // .cors(Customizer.withDefaults())
                                // .csrf(t -> t.disable())
                                .exceptionHandling(exception -> exception.authenticationEntryPoint(tokenAuthEntryPoint))
                                .addFilterBefore(tokenAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)
                                .httpBasic(httpConfig -> httpConfig.disable())
                                // .formLogin(Customizer.withDefaults());
                                .formLogin(formConfig -> formConfig.disable());

                return http.build();
        }

        // @Bean
        // protected RegisteredClientRepository registeredClientRepository() {
        // RegisteredClient oidcClient =
        // RegisteredClient.withId(UUID.randomUUID().toString())
        // .clientId("oidc-client")
        // .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
        // .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        // .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        // .redirectUri("http://localhost:8000/callback")
        // .postLogoutRedirectUri("http://localhost:8000/")
        // .scope(OidcScopes.OPENID)
        // .scope(OidcScopes.PROFILE)
        // .scope(OidcScopes.EMAIL)
        // .clientSettings(ClientSettings.builder().requireProofKey(true).build())
        // .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
        // .refreshTokenTimeToLive(Duration.ofHours(8)).reuseRefreshTokens(false)
        // .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build())
        // .build();

        // return new InMemoryRegisteredClientRepository(oidcClient);
        // }

        @Bean
        protected CorsConfigurationSource corsConfigurationSource() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");
                config.addAllowedOrigin("http://localhost:8000");
                config.setAllowCredentials(true);
                source.registerCorsConfiguration("/**", config);
                return source;
        }

        @Bean
        protected JWKSource<SecurityContext> jwkSource() {
                KeyPair keyPair = generateRsaKey();
                RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
                RSAKey rsaKey = new RSAKey.Builder(publicKey)
                                .privateKey(privateKey)
                                .keyID(UUID.randomUUID().toString())
                                .build();
                JWKSet jwkSet = new JWKSet(rsaKey);
                return new ImmutableJWKSet<>(jwkSet);
        }

        private static KeyPair generateRsaKey() {
                KeyPair keyPair;
                try {
                        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                        keyPairGenerator.initialize(4096);
                        keyPair = keyPairGenerator.generateKeyPair();
                } catch (Exception ex) {
                        throw new IllegalStateException(ex);
                }
                return keyPair;
        }

        @Bean
        protected JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
                return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
        }

        @Bean
        protected JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
                return new NimbusJwtEncoder(jwkSource);
        }

        @Bean
        protected PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        protected CompromisedPasswordChecker passwordChecker() {
                return new HaveIBeenPwnedRestApiPasswordChecker();
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        protected AuthorizationServerSettings authorizationServerSettings() {
                return AuthorizationServerSettings.builder().build();
        }

        // @Bean
        // protected OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(
        // OidcUserInfoService oidcUserInfoService) {
        // return (context) -> {

        // if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
        // context.getClaims().claims((claims) -> {
        // if (context.getAuthorizationGrantType()
        // .equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
        // Set<String> roles = context.getClaims().build().getClaim("scope");
        // claims.put("roles", roles);
        // } else if (context.getAuthorizationGrantType()
        // .equals(AuthorizationGrantType.AUTHORIZATION_CODE)) {
        // Set<String> roles = AuthorityUtils
        // .authorityListToSet(
        // context.getPrincipal().getAuthorities())
        // .stream()
        // .collect(Collectors.collectingAndThen(
        // Collectors.toSet(),
        // Collections::unmodifiableSet));
        // claims.put("roles", roles);
        // String username = (String) claims.get("sub");
        // claims.putAll(oidcUserInfoService.loadUser(username).getClaims());

        // }
        // });
        // }
        // };
        // }

}
