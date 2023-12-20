package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import io.github.douglasliebl.msproducts.model.entity.Category;
import io.github.douglasliebl.msproducts.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDTO createProduct(ProductInsertDTO request);

    ProductDTO getById(Long id);

    ProductDTO update(Long id, ProductUpdateDTO updateData);

    String delete(Long id);

    PageImpl<ProductDTO> find(String name, Pageable pageRequest);

    Page<Product> findByManufacturer(Long id, Pageable pageRequest);

    Page<Product> findByCategory(Category category, Pageable pageRequest);

}
