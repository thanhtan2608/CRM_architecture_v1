package org.example.template_architecture.domain.repository;

import org.example.template_architecture.domain.entity.ProductType;

import java.util.List;
import java.util.Optional;

public interface ProductTypeRepository {
    // Hàm kiểm tra trùng tên
    boolean existsByTypeName(String typeName);

    // Hàm lưu loại sản phẩm
    ProductType save(ProductType productType);
    List<ProductType> findAll();
    Optional<ProductType> findById(Long id);
    void deleteById(Long id);
}
