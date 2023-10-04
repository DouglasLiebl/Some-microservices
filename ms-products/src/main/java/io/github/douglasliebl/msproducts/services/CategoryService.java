package io.github.douglasliebl.msproducts.services;

import io.github.douglasliebl.msproducts.dto.CategoryDTO;
import io.github.douglasliebl.msproducts.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {

    Category registerCategory(Category request);

    Optional<Category> getById(Long id);

    Category update(Category actualCategory, CategoryDTO request);

    void delete(Category category);

    Page<Category> find(String name, Pageable pageRequest);
}
