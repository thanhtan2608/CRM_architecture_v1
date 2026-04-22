package org.example.template_architecture.presentation.controller;

import jakarta.validation.Valid;
import org.example.template_architecture.application.dto.ProductTypeReqest;
import org.example.template_architecture.application.dto.ProductTypeResponse;
import org.example.template_architecture.application.input.ICreateProductType;
import org.example.template_architecture.application.input.IDeleteProductType;
import org.example.template_architecture.application.input.IGetAllProductTypes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product-types")
public class ProductTypeWebController {

    private final IGetAllProductTypes getAllProductTypes;
    private final ICreateProductType createProductType;
    private final IDeleteProductType deleteProductType;

    public ProductTypeWebController(IGetAllProductTypes getAllProductTypes,ICreateProductType createProductType,
                                    IDeleteProductType deleteProductType) {
        this.getAllProductTypes = getAllProductTypes;
        this.createProductType=createProductType;
        this.deleteProductType=deleteProductType;
    }

    @GetMapping
    public String listTypes(Model model) {
        // Gọi Usecase lấy dữ liệu
        List<ProductTypeResponse> types = getAllProductTypes.execute();

        // Đẩy vào model với tên biến 'listTypes' khớp với HTML
        model.addAttribute("listTypes", types);

        return "/list_product_type"; // Đường dẫn file HTML
    }
    @GetMapping("/new")
    public String showAddForm(Model model) {
        // Vì là record, ta phải truyền giá trị khởi tạo rỗng cho Tên và 1 cho Trạng thái
        model.addAttribute("productType", new ProductTypeReqest(null, "",null));
        return "add_product_type";
    }

    @PostMapping("/add")
    public String addProductType(@Valid @ModelAttribute("productType") ProductTypeReqest request,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add_product_type";
        }

        try {
            createProductType.execute(request);
            redirectAttributes.addFlashAttribute("message", "Thêm loại sản phẩm thành công!");
            return "redirect:/product-types";

        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("typeName", "error.productType", e.getMessage());
            return "add_product_type";
        }
    }
    // 2. API Xóa danh mục
    @GetMapping("/delete/{id}")
    public String deleteProductType(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            deleteProductType.execute(id);
            redirectAttributes.addFlashAttribute("message", "✅ Đã xóa danh mục thành công!");

        } catch (DataIntegrityViolationException e) {
            // BẮT LỖI KHÓA NGOẠI TẠI ĐÂY
            redirectAttributes.addFlashAttribute("error", "❌ Không thể xóa! Danh mục này đang chứa sản phẩm. Vui lòng xóa các sản phẩm bên trong trước.");

        } catch (Exception e) {
            // Bắt các lỗi vặt khác
            redirectAttributes.addFlashAttribute("error", "❌ Đã xảy ra lỗi không xác định khi xóa!");
        }

        return "redirect:/product-types";
    }
}