package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.dto.ProductTypeResponse;
import org.example.template_architecture.application.input.IGetActiveProductTypes;
import org.example.template_architecture.application.input.IGetAllProductTypes;

import java.util.List;
import java.util.stream.Collectors;

public class GetActiveProductTypesImpl implements IGetActiveProductTypes {
    private final IGetAllProductTypes getAllProductTypes;

    public GetActiveProductTypesImpl(IGetAllProductTypes getAllProductTypes) {
        this.getAllProductTypes = getAllProductTypes;
    }

    @Override
    public List<ProductTypeResponse> execute() {
        return getAllProductTypes.execute().stream()
                .filter(t -> t.isActive() != null && t.isActive() == 1)
                .collect(Collectors.toList());
    }
}
