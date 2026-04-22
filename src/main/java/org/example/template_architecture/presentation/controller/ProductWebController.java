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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
    @RequestMapping("/products")
    public class ProductWebController {

        private final IGetAllProducts getAllProducts;
        private final IGetAllProductTypes getAllProductTypes;
        private final ICreateProduct createProduct;
        private  final ICheckProductCode checkProductCode;
        private final IDeleteProduct deleteProduct;
        private final IRestoreProduct  restoreProduct;
        private final IFindProductByCode findProductByCode;
    private final IFindProductById findProductById;
    private final IUpdateProduct updateProduct;
    private final ISearchProducts searchProducts;

        public ProductWebController(IGetAllProducts getAllProducts,IGetAllProductTypes getAllProductTypes,ICreateProduct createProduct,ICheckProductCode checkProductCode,IDeleteProduct deleteProduct
                                    ,IRestoreProduct  restoreProduct,IFindProductByCode findProductByCode,
                                    IFindProductById findProductById, IUpdateProduct updateProduct,
                                    ISearchProducts searchProducts) {
            this.getAllProducts = getAllProducts;
            this.getAllProductTypes=getAllProductTypes;
            this.createProduct = createProduct;
            this.checkProductCode=checkProductCode;
            this.deleteProduct=deleteProduct;
            this.restoreProduct=restoreProduct;
            this.findProductByCode=findProductByCode;
            this.updateProduct=updateProduct;
            this.findProductById=findProductById;
            this.searchProducts=searchProducts;
        }
@GetMapping
public String showProductList(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "typeId", required = false) Long typeId,
        @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
        @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
        @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(value = "sortField", defaultValue = "createdAt") String sortField,
        @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
        Model model) {

    int pageSize = 6;

    // 1. Làm sạch dữ liệu (Clean data)
    String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
    Long cleanTypeId = (typeId != null && typeId > 0) ? typeId : null;

    // Chuyển đổi LocalDate sang LocalDateTime để khớp với Repository
    LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
    LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

    // 2. Gọi Use Case (BẮT BUỘC ĐÚNG THỨ TỰ THAM SỐ ĐÃ CHỐT)
    // Thứ tự: page, size, keyword, typeId, minPrice, maxPrice, start, end, sortBy, sortDir
    PageResult<ProductResponse> pageResult = getAllProducts.execute(
            page,
            pageSize,
            cleanKeyword,
            cleanTypeId,
            minPrice,
            maxPrice,
            startDateTime,
            endDateTime,
            sortField,
            sortDir
    );

    // 3. Lấy dữ liệu bổ trợ cho giao diện
    List<ProductTypeResponse> types = getAllProductTypes.execute();
    String reverseSortDir = sortDir.equalsIgnoreCase("asc") ? "desc" : "asc";

    // 4. Đẩy dữ liệu ra View
    model.addAttribute("listProducts", pageResult.getItems());
    model.addAttribute("currentPage", pageResult.getCurrentPage());
    model.addAttribute("totalPages", pageResult.getTotalPages());
    model.addAttribute("totalItems", pageResult.getTotalElements());
    model.addAttribute("listTypes", types);

    // 5. Giữ trạng thái Form (Để khi bấm phân trang không bị mất lọc)
    model.addAttribute("keyword", keyword);
    model.addAttribute("typeId", typeId);
    model.addAttribute("minPrice", minPrice);
    model.addAttribute("maxPrice", maxPrice);
    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("sortField", sortField);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("reverseSortDir", reverseSortDir);

    return "list_product";
    }
        // Mở trang thêm mới sản phẩm
        @GetMapping("/new")
        public String showAddForm(Model model) {
            // 1. Tạo một đối tượng rỗng để hứng dữ liệu từ Form
            model.addAttribute("product", new ProductRequest());

            // 2. Lấy danh sách Loại sản phẩm để đổ vào dropdown
            List<ProductTypeResponse> allTypes = getAllProductTypes.execute();

            // 2. Dùng Java Stream để LỌC ra những loại đang hoạt động (isActive == 1)
            List<ProductTypeResponse> activeTypes = allTypes.stream()
                    .filter(type -> type.isActive() != null && type.isActive() == 1)
                    .collect(Collectors.toList());

            // 3. Đẩy danh sách đã lọc ra giao diện
            model.addAttribute("listTypes", activeTypes);

            return "add_product"; // Trả về tên file HTML của bạn
        }
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") ProductRequest request,
                              BindingResult bindingResult,
                              @RequestParam("fileImage") MultipartFile multipartFile,
                              RedirectAttributes redirectAttributes,
                              Model model) throws IOException {

        boolean isRestoreContext = false; // Biến cờ để đánh dấu xem đây là Thêm mới hay Phục hồi

        // 1. KIỂM TRA MÃ SẢN PHẨM (TRÙNG HOẶC ĐÃ XÓA)
        if (!bindingResult.hasFieldErrors("productCode")) {
            // Lấy thông tin sản phẩm từ DB (bao gồm cả cái đã xóa)
            // Lưu ý: Bạn cần tạo Use Case IFindProductByCode giống các Use Case khác nhé
            Optional<Product> existingProduct = findProductByCode.execute(request.getProductCode());

            if (existingProduct.isPresent()) {
                if (existingProduct.get().getIsDeleted() == 0) {
                    // Nếu đang hoạt động -> Báo lỗi trùng mã
                    bindingResult.rejectValue("productCode", "Duplicate", "⚠ Mã sản phẩm này đã tồn tại và đang hoạt động!");
                } else {
                    // Nếu đã xóa (isDeleted == 0) -> Không báo lỗi, bật cờ phục hồi lên
                    isRestoreContext = true;
                }
            }
        }

        // 2. XỬ LÝ NẾU CÓ LỖI FORM (Trống dữ liệu hoặc Trùng mã ở trên)
        if (bindingResult.hasErrors()) {
            // Nạp lại danh sách loại sản phẩm đang hoạt động
            List<ProductTypeResponse> activeTypes = getAllProductTypes.execute().stream()
                    // CHÚ Ý CHỖ NÀY: Hãy dùng tên hàm Get chuẩn xác của bạn (vd: getIsDeleted() == 0 hoặc getIsActive() == 1)
                    .filter(type -> type.isActive() != null && type.isActive() == 1)
                    .collect(Collectors.toList());
            model.addAttribute("listTypes", activeTypes);

            return "add_product";
        }

        // 3. XỬ LÝ UPLOAD ẢNH (Code của bạn viết rất chuẩn rồi)
        String fileName = "";
        if (!multipartFile.isEmpty()) {
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // 4. QUYẾT ĐỊNH: LƯU MỚI HAY PHỤC HỒI
        if (isRestoreContext) {
            // Gọi Use Case phục hồi
            restoreProduct.execute(request, fileName);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm cũ đã được khôi phục và cập nhật thông tin mới!");
        } else {
            // Gọi Use Case thêm mới bình thường
            createProduct.execute(request, fileName);
            redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm thành công!");
        }

        return "redirect:/products";
    }
        // Xử lý xóa (đường dẫn khớp với nút Xóa trong HTML)
        @GetMapping("/delete/{id}")
        public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
            try {
                deleteProduct.execute(id);
                redirectAttributes.addFlashAttribute("message", "Đã xóa sản phẩm thành công!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa sản phẩm.");
            }
            return "redirect:/products";
        }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productpt = findProductById.execute(id);

        if (productpt.isPresent()) {
            Product product = productpt.get();

            // Chuyển dữ liệu từ Entity sang DTO để nạp vào Form
            ProductRequest request = new ProductRequest();
            request.setId(product.getId());
            request.setProductCode(product.getProductCode());
            request.setImageUrl(product.getImageUrl());
            request.setName(product.getName());
            request.setPrice(product.getPrice());
            request.setDescription(product.getDescription());
            request.setTypeId(product.getTypeId());

            model.addAttribute("product", request);
            model.addAttribute("productId", id); // Truyền ID ra HTML để lưu lại

            // Nạp danh sách loại sản phẩm
            List<ProductTypeResponse> activeTypes = getAllProductTypes.execute().stream()
                    .filter(t -> t.isActive() != null && t.isActive() ==1)
                    .collect(Collectors.toList());
            model.addAttribute("listTypes", activeTypes);

            return "edit_product"; // Trả về giao diện riêng cho việc Sửa
        }

        redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
        return "redirect:/products";
    }

    // 2. API LƯU THÔNG TIN SAU KHI SỬA
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("product") ProductRequest request,
                                BindingResult bindingResult,
                                @RequestParam("fileImage") MultipartFile multipartFile,
                                RedirectAttributes redirectAttributes,
                                Model model) throws IOException {

        // Trái với hàm Save (Thêm mới), ở hàm Update ta thường KHÔNG check trùng productCode
        // vì người dùng giữ nguyên mã cũ là chuyện bình thường. Bạn có thể để ô ProductCode là read-only.

        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            List<ProductTypeResponse> activeTypes = getAllProductTypes.execute().stream()
                    .filter(t -> t.isActive() != null && t.isActive() ==1)
                    .collect(Collectors.toList());
            model.addAttribute("listTypes", activeTypes);
            return "edit_product";
        }

        // Xử lý file ảnh
        String fileName = "";
        if (!multipartFile.isEmpty()) {
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Gọi Use Case cập nhật
        updateProduct.execute(id, request, fileName);

        redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công!");
        return "redirect:/products";
    }
    }
