package org.example.template_architecture.presentation.controller;

import jakarta.validation.Valid;
import org.example.template_architecture.application.dto.ProductRequest;
import org.example.template_architecture.application.dto.ProductResponse;
import org.example.template_architecture.application.input.*;
import org.example.template_architecture.application.mapper.ProductMapper;
import org.example.template_architecture.domain.entity.PageResult;
import org.example.template_architecture.domain.entity.Product;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final IGetAllProducts getAllProducts;
    private final IDeleteProduct deleteProduct;
    private final IFindProductById findProductById;
    private final ICheckProductCode checkProductCode;
    private final IFindProductByCode findProductByCode;
    private final ICreateProduct createProduct;
    private final IUpdateProduct updateProduct;
    private  final ProductMapper productMapper;

    public ProductRestController(IGetAllProducts getAllProducts,
                                 IDeleteProduct deleteProduct,
                                 IFindProductById findProductById,
                                 ICheckProductCode checkProductCode,
                                 IFindProductByCode findProductByCode,
                                 ICreateProduct createProduct,
                                 IUpdateProduct updateProduct,
                                 ProductMapper productMapper) {
        this.getAllProducts = getAllProducts;
        this.deleteProduct = deleteProduct;
        this.findProductById = findProductById;
        this.checkProductCode = checkProductCode;
        this.findProductByCode = findProductByCode;
        this.createProduct=createProduct;
        this.updateProduct=updateProduct;
        this.productMapper=productMapper;
    }

    // 1. Lấy danh sách sản phẩm (Phân trang, Lọc, Tìm kiếm)
    @GetMapping
    public ResponseEntity<PageResult<ProductResponse>> getProductsData(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir) {

        LocalDateTime startDT = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDT = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        PageResult<ProductResponse> data = getAllProducts.execute(
                page, size, keyword, typeId, minPrice, maxPrice, startDT, endDT,sortField,sortDir
        );

        return ResponseEntity.ok(data);
    }

    // 2. Lấy chi tiết 1 sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return findProductById.execute(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Kiểm tra nhanh mã sản phẩm đã tồn tại hay chưa (Dùng khi người dùng đang nhập)
    @GetMapping("/check-code")
    public ResponseEntity<Boolean> checkCode(@RequestParam String code) {
        boolean exists = checkProductCode.execute(code);
        return ResponseEntity.ok(exists);
    }

    // 4. Xóa sản phẩm (Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        deleteProduct.execute(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        // Lưu ý: Đối với API thuần, ảnh thường được gửi dạng Base64 trong JSON
        // hoặc gửi qua một API upload file riêng. Ở đây ta giả định nhận DTO.

        // Gọi Use Case thêm mới
        // (Bạn cần điều chỉnh CreateProduct để trả về ProductResponse)
        ProductResponse newProduct = createProduct.execute(request, request.getImageUrl());

        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    // 6. API CẬP NHẬT SẢN PHẨM
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ProductRequest request) {

        // Gọi Use Case cập nhật
        updateProduct.execute(id, request, request.getImageUrl());

        // Lấy lại dữ liệu sau khi cập nhật để trả về cho Client
        return findProductById.execute(id)
                .map(productMapper::toResponse) // Map sang DTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}