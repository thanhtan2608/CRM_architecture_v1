package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.input.IFindProductByCode;
import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.domain.repository.ProductRepository;

import java.util.Optional;

public class FindProductByCodeImpl implements IFindProductByCode {

    private final ProductRepository productRepository;

    public FindProductByCodeImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> execute(String code) {
        // Hàm findByProductCode này bạn đã tạo ở bước làm chức năng Phục hồi rồi
        return productRepository.findByProductCode(code);
    }
}
