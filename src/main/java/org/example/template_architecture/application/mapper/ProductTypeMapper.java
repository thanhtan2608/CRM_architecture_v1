package org.example.template_architecture.application.mapper;

import org.example.template_architecture.application.dto.ProductTypeResponse;
import org.example.template_architecture.domain.entity.ProductType;
import org.example.template_architecture.infrastructure.persistence.ProductTypeDbEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeMapper {
    // chứ không phải DbEntity (ProductTypeDbEntity)
    public ProductTypeResponse toResponse(ProductType domain) {
        if (domain == null) return null;
        return new ProductTypeResponse(
                domain.getId(),
                domain.getTypeName(),
                domain.getIsActive()
        );
    }
    public ProductType toDomain(ProductTypeDbEntity dbEntity) {
        if (dbEntity == null) return null;
        ProductType domain = new ProductType();
        domain.setId(dbEntity.getId());
        domain.setTypeName(dbEntity.getTypeName());
        domain.setIsActive(dbEntity.getIsActive());
        // domain.setDescription(dbEntity.getDescription()); // Nhớ bật lại dòng này nếu có nhé
        return domain;
    }

    // Chuyển từ Domain Entity -> DB Entity
    public ProductTypeDbEntity toDbEntity(ProductType domain) {
        if (domain == null) return null;
        ProductTypeDbEntity db = new ProductTypeDbEntity();
        db.setId(domain.getId());
        db.setTypeName(domain.getTypeName());
        db.setIsActive(domain.getIsActive());
        // db.setDescription(domain.getDescription());
        return db;
    }
}
