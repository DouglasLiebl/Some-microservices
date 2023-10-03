package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.model.entity.Product;

public interface ProductService {

    Product createProduct(ProductInsertDTO request);
}
