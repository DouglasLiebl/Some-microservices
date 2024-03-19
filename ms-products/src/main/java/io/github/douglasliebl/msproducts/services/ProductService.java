package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import io.github.douglasliebl.msproducts.model.entity.Category;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDTO createProduct(ProductInsertDTO request);

    ProductDTO getById(Long id);

    ProductDTO update(Long id, ProductUpdateDTO updateData);

    String delete(Long id);

    PageImpl<ProductDTO> find(String name, Pageable pageRequest);

    PageImpl<ProductDTO> findByManufacturer(Long id, Pageable pageRequest);

    PageImpl<ProductDTO> findByCategory(Category category, Pageable pageRequest);

}
