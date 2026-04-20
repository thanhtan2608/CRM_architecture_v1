package org.example.template_architecture.domain.repository;

import org.example.template_architecture.domain.entity.ProductType;

import java.util.List;
import java.util.Optional;

public interface ProductTypeRepository {
    List<ProductType> findAll();
    Optional<ProductType> findById(Long id);
}
