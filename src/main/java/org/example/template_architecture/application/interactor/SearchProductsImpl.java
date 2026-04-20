package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.dto.ProductResponse;
import org.example.template_architecture.application.input.ISearchProducts;
import org.example.template_architecture.application.mapper.ProductMapper;
import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.domain.repository.ProductRepository;
import org.example.template_architecture.infrastructure.persistence.ProductDbEntity;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class SearchProductsImpl implements ISearchProducts {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public SearchProductsImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductResponse> execute(String keyword,Long typeId, String sortField, String sortDir) {
        // 1. Gọi thẳng xuống Domain Repository (Rất sạch sẽ, không vướng bận Spring Sort)
        List<Product> products = productRepository.searchAndSort(keyword,typeId, sortField, sortDir);

        // 2. Map từ Domain sang Response DTO để trả về Controller
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }
}

