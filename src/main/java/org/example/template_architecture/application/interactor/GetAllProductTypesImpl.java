package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.dto.ProductTypeResponse;
import org.example.template_architecture.application.input.IGetAllProductTypes;
import org.example.template_architecture.application.mapper.ProductTypeMapper;
import org.example.template_architecture.domain.repository.ProductTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllProductTypesImpl implements IGetAllProductTypes {
    private final ProductTypeRepository repository;
    private final ProductTypeMapper mapper;

    public GetAllProductTypesImpl(ProductTypeRepository repository, ProductTypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductTypeResponse> execute() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
