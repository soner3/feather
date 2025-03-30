package com.feather.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "Feather Authorization Server", summary = "Authorization Server implementing OAuth2 and OpenID Connect", description = "This server provides authentication and authorization services following OAuth2 and OpenID Connect standards.", version = "1.0.0", license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"), contact = @Contact(email = "sonerastan@icloud.com", name = "Soner Astan", url = "https://sonastan.com")), servers = {
		@Server(url = "http://localhost:9000", description = "Authorization Server"),
})
public class AuthserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}

}
