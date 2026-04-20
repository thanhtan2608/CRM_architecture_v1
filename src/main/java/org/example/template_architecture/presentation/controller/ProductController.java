package org.example.template_architecture.presentation.controller;

import org.example.template_architecture.application.dto.ProductRequest;
import org.example.template_architecture.application.dto.ProductResponse;
import org.example.template_architecture.application.input.ICreateProduct;
import org.example.template_architecture.application.input.IGetAllProducts;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ICreateProduct createProduct;

    public ProductController(ICreateProduct createProduct) {
        this.createProduct = createProduct;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(createProduct.execute(request,null));
    }

}
