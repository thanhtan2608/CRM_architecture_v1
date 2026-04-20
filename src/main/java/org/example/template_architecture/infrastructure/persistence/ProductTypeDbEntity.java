package org.example.template_architecture.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_types") // Khớp với ERD
@Data
public class ProductTypeDbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", length = 100, nullable = false)
    private String typeName;

    @Column(name = "is_active")
    private Integer isActive = 1; // 1: Hoạt động, 0: Tạm khóa
}