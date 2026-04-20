package org.example.template_architecture.infrastructure.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductDbEntity, Long> {
    @Query("SELECT p FROM ProductDbEntity p LEFT JOIN FETCH p.productType WHERE p.isDeleted = 0")
    List<ProductDbEntity> findAllActiveWithTypeName();
    boolean existsByProductCode(String productCode);
    @Modifying
    @Query("UPDATE ProductDbEntity p SET p.isDeleted = 1 WHERE p.id = :id")
    void softDeleteProduct(@Param("id") Long id);

    Optional<ProductDbEntity> findByProductCode(String productCode);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProductDbEntity p SET p.isDeleted = 0 WHERE p.productCode = :productCode")
    void restoreByProductCode(@Param("productCode") String productCode);

    @Query("SELECT p FROM ProductDbEntity p WHERE p.isDeleted = 0 AND " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')))"+
            "AND (:typeId IS NULL OR p.typeId = :typeId)")
    List<ProductDbEntity> searchAndSortProducts(@Param("keyword") String keyword,@Param("typeId") Long typeId, Sort sort);
}
