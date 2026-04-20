package org.example.template_architecture.application.interactor;

import jakarta.transaction.Transactional;
import org.example.template_architecture.application.dto.ProductRequest;
import org.example.template_architecture.application.input.IRestoreProduct;
import org.example.template_architecture.application.mapper.ProductMapper;
import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.domain.repository.ProductRepository;

import java.util.Optional;

@Transactional
public class RestoreProductImpl implements IRestoreProduct {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public RestoreProductImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public void execute(ProductRequest request, String fileName) {
        // 1. Tìm sản phẩm lên trước
        Optional<Product> existingProduct = productRepository.findByProductCode(request.getProductCode());

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            // 2. Gán isDeleted = 0 trực tiếp
            product.setIsDeleted(0);

            // 3. Cập nhật các thông tin khác từ Form
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setDescription(request.getDescription());
            product.setTypeId(request.getTypeId());

            if (fileName != null && !fileName.isEmpty()) {
                product.setImageUrl(fileName);
            }

            // 4. Lưu một lần duy nhất, Hibernate sẽ tự động UPDATE toàn bộ (gồm cả số 0)
            productRepository.save(product);
        }
    }
}
