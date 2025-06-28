package net.jasonchestnut.systolic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	info = @Info(
		title = "Systolic API",
		version = "1.0.0",
		description = "API for Systolic, a web-based tool for managing and visualizing systolic data."
	),
	servers = @Server(url = "/api")
)
@SpringBootApplication
public class SystolicApplication {
	public static void main(String[] args) {
		SpringApplication.run(SystolicApplication.class, args);
	}

}
