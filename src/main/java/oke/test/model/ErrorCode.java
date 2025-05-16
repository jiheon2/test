package oke.test.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 에러 코드")
public enum ErrorCode {
    @Schema(description = "404 사용자를 찾을 수 없음")
    USER_NOT_FOUND,

    @Schema(description = "400 잘못된 요청")
    INVALID_REQUEST,

    @Schema(description = "500 내부 서버 오류")
    INTERNAL_ERROR,

    @Schema(description = "403 권한 없음")
    UNAUTHORIZED,

    @Schema(description = "403 접근 금지")
    FORBIDDEN,

    @Schema(description = "409 이미 존재하는 리소스")
    ALREADY_EXISTS,

    @Schema(description = "400 필수 파라미터 누락")
    MISSING_PARAMETER,

    @Schema(description = "405 지원하지 않는 메서드")
    METHOD_NOT_ALLOWED,

    @Schema(description = "400 요청 데이터 형식 오류")
    INVALID_FORMAT
}
