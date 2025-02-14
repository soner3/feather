package com.feather.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	protected RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(r -> r
						.path("/feather/authserver/**")
						.filters(f -> f.rewritePath("/feather/authserver/(?<segment>.*)", "${segment}"))
						.uri("http://localhost:9000"))
				.route(r -> r
						.path("/feather/profile/**")
						.filters(f -> f.rewritePath("/feather/profile/(?<segment>.*)", "/${segment}"))
						.uri("http://localhost:8080"))
				.route(r -> r
						.path("/**")
						.uri("http://localhost:3000"))
				.build();
	}

}
