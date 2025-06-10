package com.example.springbootzero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Product API",
        version = "1.0",
        description = "API REST pour la gestion de produits et bundles"
    )
)
@SpringBootApplication
public class SpringbootzeroApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootzeroApplication.class, args);
	}

}
