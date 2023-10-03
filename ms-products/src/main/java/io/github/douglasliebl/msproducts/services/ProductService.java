package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import io.github.douglasliebl.msproducts.model.entity.Product;

import java.util.Optional;

public interface ProductService {

    Product createProduct(ProductInsertDTO request);

    Optional<Product> getById(Long id);

    Product update(Product actualProduct, ProductUpdateDTO updateData);
}
