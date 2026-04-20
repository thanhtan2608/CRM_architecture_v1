package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.dto.ProductResponse;
import org.example.template_architecture.application.input.IGetAllProducts;
import org.example.template_architecture.application.mapper.ProductMapper;
import org.example.template_architecture.domain.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllProductsImpl implements IGetAllProducts {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public GetAllProductsImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductResponse> execute() {
        // Lấy danh sách Entity từ tầng Domain thông qua Repository
        return productRepository.findAll()
                .stream()
                // Chỉ lấy những sản phẩm chưa bị xóa (nếu bạn dùng soft delete)
                .filter(p -> p.getIsDeleted() == null || p.getIsDeleted() == 0)
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }
}
