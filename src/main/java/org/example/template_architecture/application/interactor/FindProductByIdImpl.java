package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.input.IFindProductByCode;
import org.example.template_architecture.application.input.IFindProductById;
import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.domain.repository.ProductRepository;

import java.util.Optional;

public class FindProductByIdImpl implements IFindProductById {
    private final ProductRepository productRepository;
    public FindProductByIdImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public Optional<Product> execute(Long id) {
        return productRepository.findById(id);
    }
}
