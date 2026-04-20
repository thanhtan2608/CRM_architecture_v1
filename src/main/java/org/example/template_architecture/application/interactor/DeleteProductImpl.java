package org.example.template_architecture.application.interactor;

import jakarta.transaction.Transactional;
import org.example.template_architecture.application.input.IDeleteProduct;
import org.example.template_architecture.domain.repository.ProductRepository;

public class DeleteProductImpl implements IDeleteProduct {
    private final ProductRepository productRepository;

    public DeleteProductImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional // Rất quan trọng khi dùng @Modifying UPDATE
    public void execute(Long id) {
        productRepository.deleteById(id);
    }
}
