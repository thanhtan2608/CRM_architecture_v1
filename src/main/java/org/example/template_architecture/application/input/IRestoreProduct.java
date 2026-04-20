package org.example.template_architecture.application.input;

import org.example.template_architecture.application.dto.ProductRequest;

public interface IRestoreProduct {
    void execute(ProductRequest request, String fileName);
}
