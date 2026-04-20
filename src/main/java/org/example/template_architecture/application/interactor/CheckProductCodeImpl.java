package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.input.ICheckProductCode;
import org.example.template_architecture.domain.repository.ProductRepository;

public class CheckProductCodeImpl implements ICheckProductCode {
    private final ProductRepository productRepository;

    public CheckProductCodeImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean execute(String code) {
        return productRepository.existsByProductCode(code);
    }
}
