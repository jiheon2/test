package oke.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import oke.test.config.ApiException;
import oke.test.model.ErrorCode;
import oke.test.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "User", description = "Okestro 사원 관련 API")
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>(List.of(
            new User(1, "박광은", "공공클라우드네이티브실", "이행관리팀"),
            new User(2, "김성경", "공공클라우드네이티브실", "이행관리팀"),
            new User(3, "김지헌", "공공클라우드네이티브실", "이행관리팀"),
            new User(4, "김소영", "공공클라우드네이티브실", "이행관리팀"),
            new User(5, "김민재", "공공클라우드네이티브실", "이행관리팀"),
            new User(6, "김민호", "공공클라우드네이티브실", "아키텍트팀"),
            new User(7, "강병주", "공공클라우드네이티브실", "아키텍트팀"),
            new User(8, "조인환", "공공클라우드네이티브실", "아키텍트팀"),
            new User(9, "주수연", "공공클라우드네이티브실", "아키텍트팀")
    ));

    @GetMapping("/list")
    @Operation(summary = "오케스트로 사원 전체 조회", description = "사원 전체를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "유저 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<User>> getUserList() {
        if (users.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND, "등록된 사원이 없습니다.");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/dept/{dept}")
    @Operation(summary = "오케스트로 사원 부서별 조회", description = "부서를 기준으로 사원을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "유저 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name="dept", description = "조회할 부서명", required = true, example = "공공클라우드네이티브실", in = ParameterIn.PATH)
    public ResponseEntity<List<User>> getUserByDept(@PathVariable("dept") String dept) {
        if (dept == null || dept.isBlank()) {
            throw new ApiException(ErrorCode.MISSING_PARAMETER, "부서명은 필수입니다.");
        }
        List<User> result = users.stream()
                .filter(user -> user.dept().equals(dept))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND, "해당 부서에 사원이 없습니다.");
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/team/{team}")
    @Operation(summary = "오케스트로 사원 팀별 조회", description = "팀을 기준으로 사원을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "유저 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "team", description = "조회할 팀명", required = true, example = "이행관리팀", in = ParameterIn.PATH)
    public ResponseEntity<List<User>> getUserByTeam(@PathVariable("team") String team) {
        if (team == null || team.isBlank()) {
            throw new ApiException(ErrorCode.MISSING_PARAMETER, "팀명은 필수입니다.");
        }
        List<User> result = users.stream()
                .filter(user -> user.team().equals(team))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND, "해당 팀에 사원이 없습니다.");
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "오케스트로 사원 조회", description = "ID를 기준으로 사원을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "유저 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "조회할 사원의 ID", required = true, example = "1", in = ParameterIn.PATH)
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        if (id <= 0) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "ID는 1 이상의 정수여야 합니다.");
        }
        return users.stream()
                .filter(user -> user.id() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));
    }

    @PostMapping("/user")
    @Operation(summary = "오케스트로 사원 생성", description = "새로운 사원을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 리소스"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.name() == null || user.name().isBlank()) {
            throw new ApiException(ErrorCode.MISSING_PARAMETER, "이름은 필수 입력값입니다.");
        }
        if (users.stream().anyMatch(u -> u.id() == user.id())) {
            throw new ApiException(ErrorCode.ALREADY_EXISTS, "이미 존재하는 사원입니다.");
        }
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/user/{id}")
    @Operation(summary = "오케스트로 사원 정보 수정", description = "사원의 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "유저 없음"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 리소스"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "수정할 사원의 ID", required = true, example = "1", in = ParameterIn.PATH)
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User updateUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id() == id) {
                if (updateUser.name() == null || updateUser.name().isBlank()) {
                    throw new ApiException(ErrorCode.MISSING_PARAMETER, "이름은 필수 입력값입니다.");
                }
                // ID 중복 체크는 필요 없지만, 예시로 추가
                if (users.stream().anyMatch(u -> u.id() == updateUser.id() && u.id() != id)) {
                    throw new ApiException(ErrorCode.ALREADY_EXISTS, "이미 존재하는 사원입니다.");
                }
                User newUser = new User(id, updateUser.name(), updateUser.dept(), updateUser.team());
                users.set(i, newUser);
                return ResponseEntity.ok(newUser);
            }
        }
        throw new ApiException(ErrorCode.USER_NOT_FOUND, "해당 사용자를 찾을 수 없습니다.");
    }

    @DeleteMapping("/user/{id}")
    @Operation(summary = "오케스트로 사원 삭제", description = "ID를 기준으로 사원을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "유저 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "삭제할 사원의 ID", required = true, example = "1", in = ParameterIn.PATH)
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
        boolean removed = users.removeIf(user -> user.id() == id);
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ApiException(ErrorCode.USER_NOT_FOUND, "해당 사용자를 찾을 수 없습니다.");
        }
    }
}
