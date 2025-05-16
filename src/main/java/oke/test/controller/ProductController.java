package oke.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import oke.test.config.ApiException;
import oke.test.model.ErrorCode;
import oke.test.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product", description = "Okestro 제품 관련 API")
public class ProductController {

    // 샘플 데이터 (실제 환경에서는 서비스/DB 연동)
    private final List<Product> products = new ArrayList<>(Arrays.asList(
            new Product(1, "contrabass", "v1"),
            new Product(2, "viola-paas", "v1"),
            new Product(3, "trombone", "v2"),
            new Product(4, "okestro-cmp", "v1"),
            new Product(5, "symphony-ai", "v3"),
            new Product(6, "clarinet", "v1")
    ));

    @GetMapping("/list")
    @Operation(summary = "제품 전체 조회", description = "모든 제품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "제품 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<Product>> getProductList() {
        if (products.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND, "등록된 제품이 없습니다.");
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "제품 조회", description = "ID로 제품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "제품 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "조회할 제품의 ID", required = true, example = "1", in = ParameterIn.PATH)
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        if (id <= 0) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "ID는 1 이상의 정수여야 합니다.");
        }
        return products.stream()
                .filter(product -> product.id() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "해당 제품을 찾을 수 없습니다."));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "제품명으로 조회", description = "제품명을 기준으로 제품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "제품 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "name", description = "조회할 제품의 이름", required = true, example = "contrabass", in = ParameterIn.PATH)
    public ResponseEntity<List<Product>> getProductByName(@PathVariable("name") String name) {
        if (name == null || name.isBlank()) {
            throw new ApiException(ErrorCode.MISSING_PARAMETER, "제품명은 필수 입력값입니다.");
        }
        List<Product> result = products.stream()
                .filter(product -> product.name().equalsIgnoreCase(name))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND, "해당 이름의 제품이 없습니다.");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "제품 등록", description = "새로운 제품을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 제품"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (product.name() == null || product.name().isBlank()) {
            throw new ApiException(ErrorCode.MISSING_PARAMETER, "제품명은 필수 입력값입니다.");
        }
        if (product.version() == null || product.version().isBlank()) {
            throw new ApiException(ErrorCode.MISSING_PARAMETER, "버전은 필수 입력값입니다.");
        }
        if (products.stream().anyMatch(p -> p.id() == product.id())) {
            throw new ApiException(ErrorCode.ALREADY_EXISTS, "이미 존재하는 제품입니다.");
        }
        products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "제품 정보 수정", description = "제품 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "제품 없음"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 제품"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "수정할 제품의 ID", required = true, example = "1", in = ParameterIn.PATH)
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody Product updateProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).id() == id) {
                if (updateProduct.name() == null || updateProduct.name().isBlank()) {
                    throw new ApiException(ErrorCode.MISSING_PARAMETER, "제품명은 필수 입력값입니다.");
                }
                if (updateProduct.version() == null || updateProduct.version().isBlank()) {
                    throw new ApiException(ErrorCode.MISSING_PARAMETER, "버전은 필수 입력값입니다.");
                }
                // ID 중복 체크는 필요 없지만, 예시로 추가
                if (products.stream().anyMatch(p -> p.id() == updateProduct.id() && p.id() != id)) {
                    throw new ApiException(ErrorCode.ALREADY_EXISTS, "이미 존재하는 제품입니다.");
                }
                Product newProduct = new Product(id, updateProduct.name(), updateProduct.version());
                products.set(i, newProduct);
                return ResponseEntity.ok(newProduct);
            }
        }
        throw new ApiException(ErrorCode.USER_NOT_FOUND, "해당 제품을 찾을 수 없습니다.");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "제품 삭제", description = "ID로 제품을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "제품 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "id", description = "삭제할 제품의 ID", required = true, example = "1", in = ParameterIn.PATH)
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") int id) {
        boolean removed = products.removeIf(product -> product.id() == id);
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ApiException(ErrorCode.USER_NOT_FOUND, "해당 제품을 찾을 수 없습니다.");
        }
    }
}
