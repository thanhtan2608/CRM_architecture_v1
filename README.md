# CRM_architecture_v1

## ✨ Các tính năng nổi bật

* **CRUD Hoàn chỉnh:** Thêm, xem, sửa, và xóa mềm (Soft Delete) sản phẩm.
* **Phục hồi dữ liệu:** Khôi phục các sản phẩm đã bị xóa mềm một cách dễ dàng.
* **Tìm kiếm & Lọc:** Tìm kiếm theo tên, mã sản phẩm và lọc theo Loại sản phẩm.
* **Sắp xếp động (Dynamic Sorting):** Sắp xếp danh sách theo nhiều tiêu chí (Giá, Tên, Ngày tạo...) với mũi tên tương tác trực quan.
* **Quản lý tệp tin:** Hỗ trợ upload và hiển thị hình ảnh sản phẩm (có tính năng xem trước ảnh - Preview).
* **Validation:** Kiểm tra tính hợp lệ của Form (rỗng dữ liệu, trùng lặp mã sản phẩm) kèm thông báo lỗi thân thiện.

## 🛠️ Công nghệ sử dụng

* **Back-end:** Java 17+, Spring Boot 3.x
* **ORM & Database:** Spring Data JPA, Hibernate, MySQL
* **Front-end:** HTML5, CSS3, JavaScript thuần, Thymeleaf

## 🏗️ Kiến trúc dự án (Clean Architecture)

Dự án được chia thành 4 lớp (Layers) độc lập như sau:

### 1. 🟢 Lớp Cốt Lõi (Domain Layer)
* **Thư mục:** `src/main/java/.../domain/`
* **Vai trò:** Trái tim của hệ thống. Chứa các thực thể nghiệp vụ (Entities) và các giao thức (Interfaces) để giao tiếp với dữ liệu.
* **Đặc điểm:** Hoàn toàn là Java thuần (POJO). **Không chứa** bất kỳ Annotation nào của Spring Framework hay JPA (như `@Entity`, `@Table`, `@Autowired`).
* **Thành phần chính:**
  * `entity/Product.java`: Thực thể nghiệp vụ cốt lõi.
  * `repository/ProductRepository.java`: Interface định nghĩa các hành động lưu trữ (hợp đồng dữ liệu).

### 2. 🟡 Lớp Ứng Dụng (Application Layer)
* **Thư mục:** `src/main/java/.../application/`
* **Vai trò:** Chứa các Use Case  định nghĩa những gì hệ thống có thể làm. Đóng vai trò làm nhạc trưởng điều phối các luồng dữ liệu.
* **Đặc điểm:** Chỉ phụ thuộc vào Domain Layer. Giao tiếp với thế giới bên ngoài thông qua DTOs.
* **Thành phần chính:**
  * `input/`: Các Interface Use Case (vd: `ISearchProducts`, `IUpdateProduct`).
  * `interactor/`: Class thực thi logic Use Case (vd: `SearchProductsImpl`).
  * `dto/`: Đối tượng truyền tải dữ liệu (`ProductRequest`, `ProductResponse`).
  * `mapper/`: Chuyển đổi qua lại giữa Entity và DTO (`ProductMapper`).

### 3. 🔴 Lớp Hạ Tầng (Infrastructure Layer)
* **Thư mục:** `src/main/java/.../infrastructure/`
* **Vai trò:** Nơi giao tiếp với các công cụ bên ngoài (Database, API, Framework). Đây là lớp chứa các công nghệ cụ thể.
* **Thành phần chính:**
  * `persistence/`: Chứa các Entity của Hibernate (`ProductDbEntity`), `JpaRepository`, và class thực thi `ProductRepositoryImpl` (kế thừa từ Domain). Ở đây, dữ liệu DB sẽ được map ngược lại thành Domain Entity trước khi đẩy vào trong.
  * `configuration/`: Nơi khởi tạo các Bean thủ công (`ProductBeanConfig`), tiêm các Dependency vào Use Case để Spring Boot quản lý.

### 4. 🔵 Lớp Trình Diễn (Presentation Layer)
* **Thư mục:** `src/main/java/.../presentation/`
* **Vai trò:** Chịu trách nhiệm giao tiếp với người dùng (nhận HTTP Request và trả về HTML/JSON).
* **Thành phần chính:**
  * `controller/ProductWebController.java`: Nơi bắt các endpoint (GET, POST), gọi các Use Case ở Application Layer và đẩy dữ liệu ra Thymeleaf.
  * (Views) `src/main/resources/templates/`: Các file HTML Thymeleaf (`list_product.html`, `edit_product.html`).

---
🚀 Cài Đặt Nhanh
Clone dự án.

Sửa thông tin DB trong application.properties.

Khởi chạy ứng dụng: Chạy class main TemplateArchitectureApplication.java

Chạy mvn spring-boot:run.

Truy cập http://localhost:8080/products.
