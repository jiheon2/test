package oke.test.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 에러 응답")
public record ErrorResponse(
        @Schema(description = "에러 코드", example = "USER_NOT_FOUND")
        ErrorCode code,

        @Schema(description = "에러 메시지", example = "해당 사용자를 찾을 수 없습니다.")
        String message
) {
}
