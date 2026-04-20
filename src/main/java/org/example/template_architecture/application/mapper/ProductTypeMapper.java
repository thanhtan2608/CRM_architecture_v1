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
}
