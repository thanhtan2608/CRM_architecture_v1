package org.example.template_architecture.application.input;

import org.example.template_architecture.application.dto.ProductRequest;
import org.example.template_architecture.application.dto.ProductResponse;

public interface ICreateProduct {
    ProductResponse execute(ProductRequest request, String fileName);
}
