package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.dto.ProductTypeReqest;
import org.example.template_architecture.application.input.ICreateProductType;
import org.example.template_architecture.domain.entity.ProductType;
import org.example.template_architecture.domain.repository.ProductTypeRepository;

public class CreateProductTypeImpl implements ICreateProductType {
    private final ProductTypeRepository productTypeRepository;

    public CreateProductTypeImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public void execute(ProductTypeReqest request) {
        // 1. Kiểm tra trùng lặp tên loại sản phẩm
        if (productTypeRepository.existsByTypeName(request.typeName())) {
            throw new IllegalArgumentException("Tên loại sản phẩm này đã tồn tại!");
        }

        // 2. Chuyển từ DTO sang Domain Entity
        ProductType newType = new ProductType();
        newType.setTypeName(request.typeName());
        newType.setIsActive(request.isActive() != null ? request.isActive() : 1); // Mặc định là 1 (Đang hoạt động)

        // 3. Lưu xuống Database
        productTypeRepository.save(newType);
    }
}
