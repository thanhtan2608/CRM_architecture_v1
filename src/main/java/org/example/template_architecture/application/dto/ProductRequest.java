package org.example.template_architecture.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class ProductRequest {
    private Long id;
    @NotBlank(message = "⚠ Mã sản phẩm không được để trống")
    private String productCode;
    private String imageUrl;
    @NotBlank(message = "⚠ Tên sản phẩm không được để trống")
    private String name;
    @NotNull(message = "⚠ Giá sản phẩm không được để trống")
    @Min(value = 0, message = "⚠ Giá sản phẩm không được âm")
    private BigDecimal price;
    private String description;
    @NotNull(message = "⚠ Vui lòng chọn loại sản phẩm")
    private Long typeId;


    public ProductRequest() {
    }

}