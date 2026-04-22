package org.example.template_architecture.application.interactor;

import org.example.template_architecture.application.input.IDeleteProductType;
import org.example.template_architecture.domain.repository.ProductTypeRepository;

public class DeleteProductTypeImpl implements IDeleteProductType {
    private final ProductTypeRepository productTypeRepository;

    public DeleteProductTypeImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public void execute(Long id) {
        // Bạn có thể thêm logic kiểm tra trước khi xóa ở đây (vd: ID có tồn tại không)
        productTypeRepository.deleteById(id);
    }
}
