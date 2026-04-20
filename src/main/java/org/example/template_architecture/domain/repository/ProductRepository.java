package org.example.template_architecture.domain.repository;

import org.example.template_architecture.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    boolean existsByProductCode(String code);
    void deleteById(Long id);
    Optional<Product> findByProductCode(String code);
    void restore(String code);
    List<Product> searchAndSort(String keyword, Long typeId, String sortField, String sortDir);
}
