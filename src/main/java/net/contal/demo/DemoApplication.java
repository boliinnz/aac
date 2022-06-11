package net.contal.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Bank Transaction APIs",
				version = "1.0",
				description = "Documentation APIs v1.0",
				contact = @io.swagger.v3.oas.annotations.info.Contact(
						name = "Bo Li",
						email = "li_edge@hotmail.com"
				)
		)
)
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
