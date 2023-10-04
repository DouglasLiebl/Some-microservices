package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import io.github.douglasliebl.msproducts.model.entity.Manufacturer;
import io.github.douglasliebl.msproducts.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Product createProduct(ProductInsertDTO request);

    Optional<Product> getById(Long id);

    Product update(Product actualProduct, ProductUpdateDTO updateData);

    void delete(Product product);

    Page<Product> find(String name, Pageable pageRequest);

    Page<Product> findByManufacturer(Manufacturer manufacturer, Pageable pageRequest);
}
