package oke.test.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Okestro 유저 정보",
        example = """
                    {
                        "id": 1,
                        "name": "김지헌",
                        "dept": "공공클라우드네이티브실",
                        "team": "이행관리팀"
                    }
                """
)
@Builder
public record User(

        @Schema(description = "사원 고유 번호", example = "1", format = "int64")
        int id,

        @Schema(description = "사원 이름", example = "김지헌", format = "String")
        String name,

        @Schema(description = "사원 소속 부서", example = "공공클라우드네이티브실", format = "String")
        String dept,

        @Schema(description = "사원 소속 팀", example = "이행관리팀", format = "String")
        String team
) {
}
