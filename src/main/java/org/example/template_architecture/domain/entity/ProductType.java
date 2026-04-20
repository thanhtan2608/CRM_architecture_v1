package org.example.template_architecture.domain.entity;

public class ProductType {
    private Long id;
    private String typeName;
    private Integer isActive;

    public ProductType(Long id, String typeName, Integer isActive) {
        this.id = id;
        this.typeName = typeName;
        this.isActive = isActive;
    }

    public ProductType() {
    }

    public Integer getIsActive() {
        return isActive;
    }

    public String getTypeName() {
        return typeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
