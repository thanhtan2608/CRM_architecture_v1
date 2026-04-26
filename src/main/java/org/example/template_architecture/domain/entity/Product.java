package org.example.template_architecture.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private Long id;
    private String productCode;
    private Long typeId;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isDeleted;
    private String typeName;

    public Product(Long id, String productCode, Long typeId, String name, BigDecimal price, String imageUrl, String description, LocalDateTime createdAt, LocalDateTime updatedAt, Integer isDeleted, String typeName) {
        this.id = id;
        this.productCode = productCode;
        this.typeId = typeId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.typeName = typeName;
    }
    // --- NHÓM LOGIC NGHIỆP VỤ ---

    /**
     * Tự kiểm tra tính hợp lệ của đối tượng (Self-Validation)
     * Ngăn chặn việc dữ liệu sai trái đi sâu vào hệ thống.
     */
    public void validate() {
        if (this.productCode == null || this.productCode.isBlank()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống.");
        }
        if (this.name == null || this.name.isBlank()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        if (this.price == null || this.price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá sản phẩm không được là số âm.");
        }
        if (this.typeId == null || this.typeId <= 0) {
            throw new IllegalArgumentException("Loại sản phẩm chưa được chọn.");
        }
    }

    /**
     * Logic cập nhật giá có kiểm soát
     */
    public void changePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá mới không hợp lệ.");
        }
        this.price = newPrice;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Logic Xóa mềm (Soft Delete) - Bảo vệ dữ liệu
     */
    public void markAsDeleted() {
        this.isDeleted = 1;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Kiểm tra sản phẩm còn hoạt động hay không
     */
    public boolean isActive() {
        return this.isDeleted != null && this.isDeleted == 0;
    }

    public Product() {
    }

    public String getTypeName() {
        return typeName;
    }


    public Long getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public Long getTypeId() {
        return typeId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
