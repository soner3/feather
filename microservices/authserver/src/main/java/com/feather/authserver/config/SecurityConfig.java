package com.feather.authserver.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.feather.authserver.config.user.OidcUserInfoService;
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

        @Value("${spring.security.oauth2.authorizationserver.issuer}")
        private String issuer;

        @Bean
        @Order(1)
        protected SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
                        throws Exception {
                OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
                                .authorizationServer();

                http
                                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                                .with(authorizationServerConfigurer, (authorizationServer) -> authorizationServer
                                                .oidc(Customizer.withDefaults()))
                                .authorizeHttpRequests((authorize) -> authorize
                                                .anyRequest().authenticated())
                                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                                .exceptionHandling((exceptions) -> exceptions
                                                .defaultAuthenticationEntryPointFor(
                                                                new LoginUrlAuthenticationEntryPoint("/login"),
                                                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

                return http.cors(Customizer.withDefaults()).build();
        }

        @Bean
        @Order(2)
        protected SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
                        throws Exception {
                http
                                .authorizeHttpRequests((authorize) -> authorize
                                                .anyRequest().authenticated())
                                .formLogin(Customizer.withDefaults());

                return http.cors(Customizer.withDefaults()).build();
        }

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
        protected RegisteredClientRepository registeredClientRepository() {
                RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                                .clientId("oidc-client")
                                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                                .redirectUri("http://localhost:8000/callback")
                                .redirectUri("http://localhost:8000/silent-renew")
                                .postLogoutRedirectUri("http://localhost:8000/")
                                .scope(OidcScopes.OPENID)
                                .clientSettings(ClientSettings.builder().requireProofKey(true).build())
                                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
                                                .refreshTokenTimeToLive(Duration.ofHours(2)).reuseRefreshTokens(false)
                                                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build())
                                .build();

                return new InMemoryRegisteredClientRepository(oidcClient);
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
        protected PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        protected CompromisedPasswordChecker passwordChecker() {
                return new HaveIBeenPwnedRestApiPasswordChecker();
        }

        @Bean
        protected AuthorizationServerSettings authorizationServerSettings() {
                return AuthorizationServerSettings
                                .builder()
                                .issuer(issuer)
                                .build();
        }

        @Bean
        protected OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(
                        OidcUserInfoService oidcUserInfoService) {
                return (context) -> {
                        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                                context.getClaims().claims(claims -> {
                                        // System.out.println(claims.get("sub")); // username
                                        Set<String> roles = AuthorityUtils
                                                        .authorityListToSet(context.getPrincipal().getAuthorities())
                                                        .stream()
                                                        .collect(Collectors.collectingAndThen(Collectors.toSet(),
                                                                        Collections::unmodifiableSet));
                                        claims.put("roles", roles);
                                        OidcUserInfo userInfo = oidcUserInfoService
                                                        .loadUser((String) claims.get("sub"));
                                        claims.putAll(userInfo.getClaims());
                                        // System.out.println(claims.get("sub")); // user-id
                                });

                        }

                        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                                context.getClaims().claims((claims) -> {
                                        if (context.getAuthorizationGrantType()
                                                        .equals(AuthorizationGrantType.AUTHORIZATION_CODE)) {
                                                Set<String> roles = AuthorityUtils
                                                                .authorityListToSet(
                                                                                context.getPrincipal().getAuthorities())
                                                                .stream()
                                                                .collect(Collectors.collectingAndThen(
                                                                                Collectors.toSet(),
                                                                                Collections::unmodifiableSet));
                                                claims.put("roles", roles);
                                                String username = (String) claims.get("sub");
                                                claims.putAll(oidcUserInfoService.loadUser(username).getClaims());

                                        }
                                });
                        }
                };
        }

}
