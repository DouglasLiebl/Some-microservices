package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.CategoryDTO;
import io.github.douglasliebl.msproducts.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {

    CategoryDTO registerCategory(CategoryDTO request);

    CategoryDTO getById(Long id);

    CategoryDTO update(Long id, CategoryDTO request);

    String delete(Long id);

    Page<Category> find(String name, Pageable pageRequest);
}
