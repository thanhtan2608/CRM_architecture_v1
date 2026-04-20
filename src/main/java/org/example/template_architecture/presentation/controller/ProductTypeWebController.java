package org.example.template_architecture.presentation.controller;

import org.example.template_architecture.application.dto.ProductTypeResponse;
import org.example.template_architecture.application.input.IGetAllProductTypes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product-types")
public class ProductTypeWebController {

    private final IGetAllProductTypes getAllProductTypes;

    public ProductTypeWebController(IGetAllProductTypes getAllProductTypes) {
        this.getAllProductTypes = getAllProductTypes;
    }

    @GetMapping
    public String listTypes(Model model) {
        // Gọi Usecase lấy dữ liệu
        List<ProductTypeResponse> types = getAllProductTypes.execute();

        // Đẩy vào model với tên biến 'listTypes' khớp với HTML
        model.addAttribute("listTypes", types);

        return "/list_product_type"; // Đường dẫn file HTML
    }
}