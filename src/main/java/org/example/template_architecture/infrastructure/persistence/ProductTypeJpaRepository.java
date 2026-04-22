package org.example.template_architecture.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeJpaRepository extends JpaRepository<ProductTypeDbEntity, Long> {
    boolean existsByTypeName(String typeName);
}
