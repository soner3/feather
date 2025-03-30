package com.feather.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Feather API Gateway", summary = "API Gateway providing authentication and request routing", description = "This gateway serves as a reverse proxy, enforcing security and routing requests to backend services while integrating OAuth2 and OpenID Connect.", version = "1.0.0", license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), contact = @Contact(email = "sonerastan@icloud.com", name = "Soner Astan", url = "https://sonastan.com")), servers = {
		@Server(url = "http://localhost:8000", description = "Production API Gateway"),
})

public class GatewayApplication {

	@Value("${app.authserver.url}")
	private String authServerUrl;

	@Value("${app.frontend.url}")
	private String frontendUrl;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	protected RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(r -> r
						.path("/**")
						.and()
						.path("/openapi/**").negate()
						.uri(frontendUrl))
				.build();
	}

}
