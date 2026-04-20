package org.example.template_architecture.application.input;

import org.example.template_architecture.application.dto.ProductRequest;

public interface IUpdateProduct {
    void execute(Long id, ProductRequest request, String fileName);
}
