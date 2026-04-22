package org.example.template_architecture.infrastructure.config;

import org.example.template_architecture.domain.entity.Product;
import org.example.template_architecture.domain.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@Configuration
public class DatabaseSeeder {
    @Bean
    public CommandLineRunner seedProducts(ProductRepository productRepository) {
        return args -> {
            // Chỉ insert nếu trong database chưa có dữ liệu (tránh chạy lại nhiều lần)
            if (productRepository.findAll().isEmpty()) {
                System.out.println("Đang tạo 10.000 sản phẩm mẫu...");
                List<Product> dummyProducts = new ArrayList<>();
                Random random = new Random();

                for (int i = 1; i <= 10000; i++) {
                    Product p = new Product();
                    p.setProductCode(String.format("SP-%05d", i)); // Định dạng SP-00001
                    p.setTypeId((long) (random.nextInt(2) + 1)); // Random 1 hoặc 2
                    p.setName("Sản phẩm tự động " + i);
                    p.setPrice(BigDecimal.valueOf(10000 + random.nextDouble() * 500000));
                    p.setImageUrl(random.nextBoolean() ? "hinh.jpg" : null);
                    p.setIsDeleted(random.nextInt(100) > 80 ? 1 : 0); // 20% tỉ lệ đã xóa

                    dummyProducts.add(p);

                    // Lưu theo lô (Batch) mỗi 1000 records để không tràn RAM
                    if (i % 1000 == 0) {
                        for (Product product : dummyProducts) {
                            productRepository.save(product);
                        }
                        dummyProducts.clear();
                    }
                }
                System.out.println("Tạo dữ liệu thành công!");
            }
        };
    }
}
