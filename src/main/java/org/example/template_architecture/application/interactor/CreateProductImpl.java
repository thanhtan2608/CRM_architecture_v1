package org.example.template_architecture.application.interactor;

import jakarta.transaction.Transactional;
import org.example.template_architecture.application.dto.ProductRequest;
import org.example.template_architecture.application.dto.ProductResponse;
import org.example.template_architecture.application.input.ICreateProduct;
import org.example.template_architecture.application.mapper.ProductMapper;
import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.domain.repository.ProductRepository;

public class CreateProductImpl implements ICreateProduct {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public CreateProductImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public ProductResponse execute(ProductRequest request, String fileName) {
        // 1. Dùng mapper để chuyển DTO sang Entity (đã có name, price, typeId...)
        Product product = productMapper.toEntity(request);

        // 2. Gán các giá trị mặc định hoặc dữ liệu ngoài (như ảnh)
        product.validate();
        product.setIsDeleted(0);

        product.setImageUrl(fileName);

        // 3. Lưu
        Product savedProduct = productRepository.save(product);

        // 4. Trả về Response
        return productMapper.toResponse(savedProduct);
    }

}
