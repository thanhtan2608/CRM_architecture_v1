1.Quy trình thêm Module mới cho thành viên

Khi bạn nhận một module (ví dụ: Contract), hãy thực hiện theo các bước sau và thay thế {Entity} bằng Contract:

Lớp Domain:

Tạo Entity tại domain/entity/Contract.java.

Định nghĩa Interface tại core/repository/ContractRepository.java.

Lớp Application:

Tạo các DTO tại application/usecase/dto/.

Viết logic xử lý trong application/usecase/interactor/CreateContractImpl.java.

Lớp Infrastructure:

Tạo infrastructure/persistence/ContractDbEntity.java (có các annotation @Table, @Entity).

Triển khai Interface tại infrastructure/persistence/ContractRepositoryImpl.java.

Lớp Presentation:

Tạo presentation/controller/ContractController.java để tiếp nhận Request.

2. Các quy tắc "Vàng" bắt buộc tuân thủ

Để giữ cho code luôn sạch, mọi thành viên phải tuân thủ:

Quy tắc phụ thuộc: Các lớp bên ngoài chỉ được phép phụ thuộc vào các lớp bên trong. Lớp Core tuyệt đối không được import bất kỳ lớp nào từ Application, Infrastructure hay Presentation.

Không sử dụng Annotation của Framework trong Core: Tuyệt đối không dùng @Service, @Autowired, @Entity hay @Table trong thư mục core.

Giao tiếp qua Interface: Luôn sử dụng Interface để gọi dữ liệu giữa các lớp nhằm đảm bảo tính linh hoạt (Loose Coupling).

Sử dụng DTO: Không bao giờ trả trực tiếp Entity của Database ra ngoài API. Phải thông qua lớp ResponseDTO.
