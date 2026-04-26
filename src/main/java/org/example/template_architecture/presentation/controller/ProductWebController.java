package org.example.template_architecture.presentation.controller;

import jakarta.validation.Valid;
import org.example.template_architecture.application.dto.ProductRequest;
import org.example.template_architecture.application.dto.ProductResponse;
import org.example.template_architecture.application.dto.ProductTypeResponse;
import org.example.template_architecture.application.input.*;
import org.example.template_architecture.domain.entity.PageResult;
import org.example.template_architecture.domain.entity.Product;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/products")
public class ProductWebController {

    private final IGetAllProducts getAllProducts;
    private final IGetAllProductTypes getAllProductTypes;
    private final ICreateProduct createProduct;
    private final IUpdateProduct updateProduct;
    private final IDeleteProduct deleteProduct;
    private final IFindProductById findProductById;
    private final IFindProductByCode findProductByCode;
    private final IRestoreProduct restoreProduct;
    private final IGetActiveProductTypes getActiveProductTypes; // Use Case mới
    private final IFileStorageService fileStorageService;

    public ProductWebController(IGetAllProducts getAllProducts,
                                IGetAllProductTypes getAllProductTypes,
                                ICreateProduct createProduct,
                                IUpdateProduct updateProduct,
                                IDeleteProduct deleteProduct,
                                IFindProductById findProductById,
                                IFindProductByCode findProductByCode,
                                IRestoreProduct restoreProduct,
                                IGetActiveProductTypes getActiveProductTypes,
                                IFileStorageService fileStorageService) {
        this.getAllProducts = getAllProducts;
        this.getAllProductTypes = getAllProductTypes;
        this.createProduct = createProduct;
        this.updateProduct = updateProduct;
        this.deleteProduct = deleteProduct;
        this.findProductById = findProductById;
        this.findProductByCode = findProductByCode;
        this.restoreProduct = restoreProduct;
        this.fileStorageService=fileStorageService;
        this.getActiveProductTypes=getActiveProductTypes;
    }

    // 1. HIỂN THỊ DANH SÁCH (Hỗ trợ Search, Filter, Pagination)
    @GetMapping
    public String showProductList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        String cleanKey = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        LocalDateTime startDT = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDT = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        PageResult<ProductResponse> pageResult = getAllProducts.execute(
                page, 6, cleanKey, (typeId != null && typeId > 0) ? typeId : null,
                minPrice, maxPrice, startDT, endDT, sortField, sortDir
        );

        model.addAttribute("listProducts", pageResult.getItems());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("listTypes", getAllProductTypes.execute());

        // Giữ trạng thái form
        model.addAttribute("keyword", keyword);
        model.addAttribute("typeId", typeId);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "list_product";
    }

    // 2. MỞ FORM THÊM MỚI
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("product", new ProductRequest());
        model.addAttribute("listTypes", getActiveProductTypes.execute());
        return "add_product";
    }

    // 3. XỬ LÝ LƯU (THÊM MỚI / PHỤC HỒI)
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") ProductRequest request,
                              BindingResult result,
                              @RequestParam("fileImage") MultipartFile file,
                              RedirectAttributes ra, Model model) throws IOException {

        boolean isRestore = false;
        if (!result.hasFieldErrors("productCode")) {
            Optional<Product> existing = findProductByCode.execute(request.getProductCode());
            if (existing.isPresent()) {
                if (existing.get().getIsDeleted() == 0) {
                    result.rejectValue("productCode", "Duplicate", "Mã sản phẩm đã tồn tại!");
                } else {
                    isRestore = true;
                }
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("listTypes", getActiveProductTypes.execute());
            return "add_product";
        }

        String fileName = fileStorageService.storeFile(file);
        if (isRestore) {
            restoreProduct.execute(request, fileName);
            ra.addFlashAttribute("message", "Đã khôi phục sản phẩm cũ!");
        } else {
            createProduct.execute(request, fileName);
            ra.addFlashAttribute("message", "Thêm mới thành công!");
        }
        return "redirect:/products";
    }

    // 4. MỞ FORM CHỈNH SỬA
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return findProductById.execute(id).map(p -> {
            ProductRequest req = new ProductRequest();
            req.setId(p.getId());
            req.setProductCode(p.getProductCode());
            req.setName(p.getName());
            req.setPrice(p.getPrice());
            req.setTypeId(p.getTypeId());
            req.setDescription(p.getDescription());
            req.setImageUrl(p.getImageUrl());

            model.addAttribute("product", req);
            model.addAttribute("listTypes", getActiveProductTypes.execute());
            return "edit_product";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/products";
        });
    }

    // 5. XỬ LÝ CẬP NHẬT
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("product") ProductRequest request,
                                BindingResult result, @RequestParam("fileImage") MultipartFile file,
                                RedirectAttributes ra, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("listTypes", getActiveProductTypes.execute());
            return "edit_product";
        }

        updateProduct.execute(id, request, file.isEmpty() ? null : fileStorageService.storeFile(file));
        ra.addFlashAttribute("message", "Cập nhật thành công!");
        return "redirect:/products";
    }

    // 6. XỬ LÝ XÓA
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        deleteProduct.execute(id);
        ra.addFlashAttribute("message", "Đã xóa sản phẩm!");
        return "redirect:/products";
    }

}