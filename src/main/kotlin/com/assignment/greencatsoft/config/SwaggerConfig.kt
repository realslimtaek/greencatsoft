package com.assignment.greencatsoft.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            // Swagger Title, Version 설정
            .info(Info().title("그릿캣소프트 과제전형").version("1.0.0"))
            // 서버 URL 설정
            .addServersItem(Server().url("/gs"))
    }
}
