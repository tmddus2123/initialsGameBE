package com.initialsgame.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("InitialsGame API")
				.description("오늘의 초성을 맞혀보는 초성게임 - InitialsGame Backend API")
				.version("v0.0.1")
				.contact(new Contact().name("InitialsGame Team")));
	}
}
