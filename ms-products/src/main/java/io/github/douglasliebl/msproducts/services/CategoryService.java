package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.CategoryDTO;
import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    CategoryDTO registerCategory(CategoryDTO request);

    CategoryDTO update(Long id, CategoryDTO request);

    String delete(Long id);

    PageImpl<ProductDTO> getProductsByCategory(Long id, Pageable pageRequest);

    List<CategoryDTO> getAllCategories();
}
