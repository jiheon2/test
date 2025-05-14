package oke.test.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Okestro API",
                version = "1.0.0",
                description = "Okestro API 테스트"
        )
)
public class SwaggerConfig {
}
