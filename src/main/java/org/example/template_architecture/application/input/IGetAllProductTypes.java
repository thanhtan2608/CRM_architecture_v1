package org.example.template_architecture.application.input;

import org.example.template_architecture.application.dto.ProductTypeResponse;

import java.util.List;

public interface IGetAllProductTypes {
    List<ProductTypeResponse> execute();
}
