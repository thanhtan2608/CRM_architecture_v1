package org.example.template_architecture.application.mapper;

import org.example.template_architecture.application.dto.ProductRequest;
import org.example.template_architecture.application.dto.ProductResponse;
import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.infrastructure.persistence.ProductDbEntity;

import java.time.format.DateTimeFormatter;

public class ProductMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Chuyển từ Request DTO (người dùng gửi lên) sang Domain Entity (để xử lý nghiệp vụ)
     */
    public Product toEntity(ProductRequest dto) {
        // Logic mapping thủ công hoặc dùng thư viện
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId());
        product.setImageUrl(dto.getImageUrl());
        product.setProductCode(dto.getProductCode());
        product.setTypeId(dto.getTypeId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());

        // Mặc định khi tạo mới
        product.setIsDeleted(0);

        return product;
    }
    /**
     * Chuyển từ Domain Entity sang Response DTO (để trả về cho Client)
     */
    public ProductResponse toResponse(Product entity) {
        // Logic mapping từ Entity sang Response DTO
        if (entity == null) return null;

        // Chuyển LocalDateTime sang String định dạng đẹp
        String formattedDateCreated = entity.getCreatedAt() != null
                ? entity.getCreatedAt().format(FORMATTER)
                : null;
        String formattedDateUpdate = entity.getUpdatedAt() != null
                ? entity.getUpdatedAt().format(FORMATTER)
                : null;

        return new ProductResponse(
                entity.getId(),
                entity.getProductCode(),
                entity.getName(),
                entity.getTypeName(),
                entity.getPrice(),
                entity.getImageUrl(),
                entity.getDescription(),
                formattedDateCreated,
                formattedDateUpdate,
                entity.getIsDeleted()
        );
    }
    public ProductDbEntity mapToDb(Product product) {
        if (product == null) return null;
        ProductDbEntity db = new ProductDbEntity();
        db.setId(product.getId());
        db.setProductCode(product.getProductCode());
        db.setTypeId(product.getTypeId());
        db.setName(product.getName());
        db.setPrice(product.getPrice());
        db.setImageUrl(product.getImageUrl());
        db.setDescription(product.getDescription());
        db.setIsDeleted(product.getIsDeleted());
        // createdAt và updatedAt sẽ được Hibernate tự sinh nhờ @CreationTimestamp
        return db;
    }

    public Product mapToDomain(ProductDbEntity db) {
        if (db == null) return null;
        Product domain = new Product();
        domain.setId(db.getId());
        domain.setProductCode(db.getProductCode());
        if (db.getProductType() != null) {
            domain.setTypeName(db.getProductType().getTypeName());
        } else {
            domain.setTypeName("Chưa phân loại");
        }
        domain.setName(db.getName());
        domain.setPrice(db.getPrice());
        domain.setImageUrl(db.getImageUrl());
        domain.setDescription(db.getDescription());
        domain.setIsDeleted(db.getIsDeleted());
        domain.setCreatedAt(db.getCreatedAt());
        domain.setUpdatedAt(db.getUpdatedAt());
        return domain;
    }
}
