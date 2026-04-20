package org.example.template_architecture.application.input;

import org.example.template_architecture.application.dto.ProductResponse;

import java.util.List;

public interface IGetAllProducts {
        List<ProductResponse> execute();
    }
