package org.example.template_architecture.infrastructure.config;

import org.example.template_architecture.application.input.*;
import org.example.template_architecture.application.interactor.*;
import org.example.template_architecture.application.mapper.ProductMapper;
import org.example.template_architecture.application.mapper.ProductTypeMapper;
import org.example.template_architecture.domain.repository.ProductRepository;
import org.example.template_architecture.domain.repository.ProductTypeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductBeanConfig {
    @Bean
    public ICreateProduct createProduct(ProductRepository repository, ProductMapper mapper) {
        return new CreateProductImpl(repository, mapper);
    }

    @Bean
    public ProductMapper productMapper() {
        return new ProductMapper();
    }
    @Bean
    public IGetAllProducts getAllProducts(ProductRepository repository, ProductMapper mapper) {
        return new GetAllProductsImpl(repository, mapper);
    }
    @Bean
    public ProductTypeMapper productTypeMapper() {
        return new ProductTypeMapper();
    }
    @Bean
    public IGetAllProductTypes getAllProductTypes(ProductTypeRepository repository, ProductTypeMapper mapper) {
        return new GetAllProductTypesImpl(repository, mapper);
    }
    @Bean
    public ICheckProductCode checkProductCode(ProductRepository repository) {
        return new CheckProductCodeImpl(repository);
    }
    @Bean
    public IDeleteProduct deleteProduct(ProductRepository repository) {
        return new DeleteProductImpl(repository);
    }
    @Bean
    public IRestoreProduct restoreProduct(ProductRepository repository, ProductMapper mapper) {
        return new RestoreProductImpl(repository, mapper);
    }
    @Bean
    public IFindProductByCode findProductByCode(ProductRepository repository) {
        return new FindProductByCodeImpl(repository);
    }
    @Bean
    public IFindProductById findProductById(ProductRepository repository) {
        return new FindProductByIdImpl(repository);
    }

    @Bean
    public IUpdateProduct updateProduct(ProductRepository repository) {
        return new UpdateProductImpl(repository);
    }
    @Bean
    public ISearchProducts searchProducts(ProductRepository repository, ProductMapper mapper){
        return new SearchProductsImpl(repository,mapper);
    }
    @Bean
    public ICreateProductType createProductType(ProductTypeRepository productTypeRepository) {
        return new CreateProductTypeImpl(productTypeRepository);
    }
    @Bean
    public IDeleteProductType deleteProductType(ProductTypeRepository productTypeRepository) {
        return new DeleteProductTypeImpl(productTypeRepository);
    }
    @Bean
    public IGetActiveProductTypes getActiveProductTypes(IGetAllProductTypes getAllProductTypes){
        return new GetActiveProductTypesImpl(getAllProductTypes);
    }
    @Bean
    public IFileStorageService fileStorageService(){
        return new FileStorageServiceImpl();
    }
}
