package oke.test.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Okestro 제품 정보",
        example = """
                    {
                        "id": 1,
                        "name": "Contrabass",
                        "version": "v1"
                    }
                """
)
@Builder
public record Product(
        @Schema(description = "제품 고유 번호", example = "1", format = "int64")
        int id,
        @Schema(description = "제품명", example = "Contrabass", format = "String")
        String name,
        @Schema(description = "제품 버전", example = "v1", format = "String")
        String version
) {
}
