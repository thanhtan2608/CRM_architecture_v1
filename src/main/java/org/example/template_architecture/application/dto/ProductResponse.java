package org.example.template_architecture.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse( Long id,String productCode, String name,String typeName, BigDecimal price, String imageUrl,
                              String description, String createdAt, String updatedAt,Integer isDeleted) {

}