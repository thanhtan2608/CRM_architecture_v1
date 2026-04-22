package org.example.template_architecture.domain.repository;

import org.example.template_architecture.domain.entity.PageResult;
import org.example.template_architecture.domain.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    PageResult<Product> getProducts(
            int page, int size, String keyword, Long typeId,
            BigDecimal minPrice, BigDecimal maxPrice,
            LocalDateTime start, LocalDateTime end,
            String sortBy, String sortDir
    );
}
