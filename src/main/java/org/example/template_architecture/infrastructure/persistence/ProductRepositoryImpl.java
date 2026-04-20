package org.example.template_architecture.infrastructure.persistence;

import org.example.template_architecture.application.mapper.ProductMapper;
import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.domain.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository jpaRepository;


    public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        // 1. Chuyển đổi từ Domain Entity sang DbEntity (để JPA hiểu)
        ProductDbEntity dbEntity = mapToDb(product);

        // 2. Gọi JPA để lưu vào MySQL
        ProductDbEntity savedEntity = jpaRepository.save(dbEntity);

        // 3. Chuyển ngược từ DbEntity vừa lưu xong sang Domain Entity để trả về
        return mapToDomain(savedEntity);
    }
    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    // --- Các hàm hỗ trợ Mapping nội bộ ---

    private ProductDbEntity mapToDb(Product product) {
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

    private Product mapToDomain(ProductDbEntity db) {
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
    @Override
    public boolean existsByProductCode(String code) {
        return jpaRepository.existsByProductCode(code);
    }
    @Override
    public void deleteById(Long id) {
        jpaRepository.softDeleteProduct(id);
    }
    @Override
    public Optional<Product> findByProductCode(String code) {
        return jpaRepository.findByProductCode(code)
                .map(this::mapToDomain); // Sử dụng hàm mapToDomain bạn đã viết trước đó
    }

    @Override
    public void restore(String code) {
      jpaRepository.restoreByProductCode(code);
    }
    @Override
    public List<Product> searchAndSort(String keyword,Long typeId, String sortField, String sortDir) {
        // 1. Tạo đối tượng Sort của Spring Data JPA
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        // 2. Gọi hàm ở JpaRepository mà bạn đã tạo ở bước trước
        List<ProductDbEntity> entities = jpaRepository.searchAndSortProducts(keyword,typeId, sort);

        // 3. Map từ Entity của Database sang Entity của Domain
        return entities.stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }
}
