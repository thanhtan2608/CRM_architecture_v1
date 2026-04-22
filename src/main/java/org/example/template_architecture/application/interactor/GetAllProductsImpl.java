package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.dto.ProductResponse;
import org.example.template_architecture.application.input.IGetAllProducts;
import org.example.template_architecture.application.mapper.ProductMapper;
import org.example.template_architecture.domain.entity.PageResult;
import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.domain.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public PageResult<ProductResponse> execute(int page, int size, String keyword, Long typeId, BigDecimal minPrice, BigDecimal maxPrice,
                                               LocalDateTime start, LocalDateTime end, String sortBy, String sortDir){
        // Xử lý keyword rỗng
        String searchKey = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;

        PageResult<Product> productPage = productRepository.getProducts(
                page,
                size,
                searchKey,
                typeId,     // Thêm typeId vào đúng vị trí
                minPrice,   // Truyền giá thấp nhất
                maxPrice,   // Truyền giá cao nhất
                start,      // Truyền ngày bắt đầu
                end,        // Truyền ngày kết thúc
                sortBy,     // Trường sắp xếp
                sortDir     // Hướng sắp xếp (asc/desc)
        );

        List<ProductResponse> responseItems = productPage.getItems().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseItems, productPage.getTotalPages(), productPage.getCurrentPage(), productPage.getTotalElements());
    }
}
