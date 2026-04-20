package org.example.template_architecture.application.input;

import org.example.template_architecture.domain.entity.Product;

import java.util.Optional;

public interface IFindProductById {
    Optional<Product> execute(Long id);
}
