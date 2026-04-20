package org.example.template_architecture.infrastructure.persistence;

import org.example.template_architecture.domain.entity.ProductType;
import org.example.template_architecture.domain.repository.ProductTypeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductTypeRepositoryImpl implements ProductTypeRepository {

    private final ProductTypeJpaRepository jpaRepository;

    public ProductTypeRepositoryImpl(ProductTypeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<ProductType> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain) // Tách ra hàm private cho sạch code
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductType> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    // Hàm bổ trợ để chuyển đổi (Mapping)
    private ProductType toDomain(ProductTypeDbEntity dbEntity) {
        if (dbEntity == null) return null;
        ProductType domain = new ProductType();
        domain.setId(dbEntity.getId());
        domain.setTypeName(dbEntity.getTypeName());
        domain.setIsActive(dbEntity.getIsActive());
        return domain;
    }
}