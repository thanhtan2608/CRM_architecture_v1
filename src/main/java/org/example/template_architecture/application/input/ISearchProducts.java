package org.example.template_architecture.application.input;

import org.example.template_architecture.application.dto.ProductResponse;

import java.util.List;

public interface ISearchProducts {
    List<ProductResponse> execute(String keyword,Long typeId, String sortField, String sortDir);
}
