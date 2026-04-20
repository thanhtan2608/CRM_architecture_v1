package org.example.template_architecture.application.interactor;

import jakarta.transaction.Transactional;
import org.example.template_architecture.application.dto.ProductRequest;
import org.example.template_architecture.application.input.IUpdateProduct;
import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.domain.repository.ProductRepository;

import java.util.Optional;

public class UpdateProductImpl implements IUpdateProduct {
    private final ProductRepository productRepository;

    public UpdateProductImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void execute(Long id, ProductRequest request, String fileName) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

           product.setProductCode(request.getProductCode());
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setDescription(request.getDescription());
            product.setTypeId(request.getTypeId());

            // CHỈ cập nhật ảnh NẾU người dùng có upload file mới
            if (fileName != null && !fileName.isEmpty()) {
                product.setImageUrl(fileName);
            }

            productRepository.save(product);
        }
    }
}
