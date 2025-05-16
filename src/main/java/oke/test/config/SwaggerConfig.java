package oke.test.config;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import oke.test.model.ErrorCode;
import oke.test.model.ErrorResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Okestro API",
                version = "1.0.0",
                description = "Okestro API 테스트"
        )
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("All APIs")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(errorResponseSchemaCustomizer())
                .addOperationCustomizer(globalResponses())
                .build();
    }

    @Bean
    public OpenApiCustomizer errorResponseSchemaCustomizer() {
        return openApi -> {
            // ErrorResponse와 ErrorCode 모두 스키마에 등록
            Map<String, Schema> errorResponseSchemas = ModelConverters.getInstance().read(ErrorResponse.class);
            Map<String, Schema> errorCodeSchemas = ModelConverters.getInstance().read(ErrorCode.class);
            Components components = openApi.getComponents();
            if (components == null) {
                components = new Components();
                openApi.setComponents(components);
            }
            errorResponseSchemas.forEach(components::addSchemas);
            errorCodeSchemas.forEach(components::addSchemas);
        };
    }


    @Bean
    public OperationCustomizer globalResponses() {
        return (operation, handlerMethod) -> {
            Schema<?> errorSchema = AnnotationsUtils.resolveSchemaFromType(ErrorResponse.class, null, null);

            operation.getResponses().addApiResponse("400",
                    new ApiResponse()
                            .description("잘못된 요청")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(errorSchema))));
            operation.getResponses().addApiResponse("404",
                    new ApiResponse()
                            .description("리소스를 찾을 수 없음")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(errorSchema))));
            operation.getResponses().addApiResponse("500",
                    new ApiResponse()
                            .description("서버 오류")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(errorSchema))));
            operation.getResponses().addApiResponse("401",
                    new ApiResponse()
                            .description("권한 없음")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(errorSchema))));
            operation.getResponses().addApiResponse("403",
                    new ApiResponse()
                            .description("접근 금지")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(errorSchema))));
            operation.getResponses().addApiResponse("409",
                    new ApiResponse()
                            .description("이미 존재하는 리소스")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(errorSchema))));
            operation.getResponses().addApiResponse("405",
                    new ApiResponse()
                            .description("지원하지 않는 메서드")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(errorSchema))));
            return operation;
        };
    }
}
