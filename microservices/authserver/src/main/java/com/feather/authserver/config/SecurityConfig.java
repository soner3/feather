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

import com.feather.authserver.config.user.OidcUserInfoService;
import com.feather.authserver.service.UserService;
import com.feather.lib.util.ClientRegistrationId;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        @Order(1)
        protected SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                        OidcUserInfoService oidcUserInfoService)
                        throws Exception {

                OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
                                .authorizationServer();

                http
                                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                                .with(authorizationServerConfigurer, (authorizationServer) -> authorizationServer
                                                .oidc(Customizer.withDefaults()))
                                .authorizeHttpRequests((authorize) -> authorize
                                                .anyRequest().authenticated())
                                .exceptionHandling((exceptions) -> exceptions
                                                .defaultAuthenticationEntryPointFor(
                                                                new LoginUrlAuthenticationEntryPoint("/login"),
                                                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

                return http.build();
        }

        @Bean
        @Order(2)
        protected SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
                        throws Exception {
                http

                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/**/public").permitAll()
                                                .anyRequest().authenticated())
                                .csrf(t -> t.disable())
                                .formLogin(Customizer.withDefaults());

                return http.build();
        }

        @Bean
        protected RegisteredClientRepository registeredClientRepository() {
                RegisteredClient oauth2Client = RegisteredClient.withId(UUID.randomUUID().toString())
                                .clientId(ClientRegistrationId.OAUTH2_CLIENT.toString())
                                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                                .redirectUri("https://oauth.pstmn.io/v1/vscode-callback")
                                .postLogoutRedirectUri("http://127.0.0.1:9000/")
                                .scope(OidcScopes.OPENID)
                                .clientSettings(ClientSettings.builder().requireProofKey(true).build())
                                .tokenSettings(TokenSettings
                                                .builder()
                                                .accessTokenTimeToLive(Duration.ofMinutes(1))
                                                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                                                .refreshTokenTimeToLive(Duration.ofMinutes(2))
                                                .reuseRefreshTokens(false)
                                                .build())
                                .build();

                return new InMemoryRegisteredClientRepository(oauth2Client);
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
        protected AuthorizationServerSettings authorizationServerSettings() {
                return AuthorizationServerSettings.builder().build();
        }

        @Bean
        protected OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(UserService userService,
                        OidcUserInfoService oidcUserInfoService) {
                return (context) -> {
                        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                                context.getClaims().claims((claims) -> {
                                        OidcUserInfo oidcUserInfo = oidcUserInfoService
                                                        .loadUser((String) claims.get("sub"));
                                        claims.putAll(oidcUserInfo.getClaims());

                                        Set<String> roles = AuthorityUtils
                                                        .authorityListToSet(
                                                                        context.getPrincipal().getAuthorities())
                                                        .stream()
                                                        .collect(Collectors.collectingAndThen(
                                                                        Collectors.toSet(),
                                                                        Collections::unmodifiableSet));
                                        claims.put("roles", roles);

                                });
                        }
                };
        }

        @Bean
        protected PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        protected CompromisedPasswordChecker passwordChecker() {
                return new HaveIBeenPwnedRestApiPasswordChecker();
        }

}
