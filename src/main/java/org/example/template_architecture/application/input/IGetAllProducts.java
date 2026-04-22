package org.example.template_architecture.application.input;

import org.example.template_architecture.application.dto.ProductResponse;
import org.example.template_architecture.domain.entity.PageResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IGetAllProducts {
    PageResult<ProductResponse> execute(
            int page,           // 1. Phân trang
            int size,
            String keyword,     // 2. Tìm kiếm & Lọc
            Long typeId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDateTime start,
            LocalDateTime end,
            String sortBy,      // 3. Sắp xếp
            String sortDir
    );
}
