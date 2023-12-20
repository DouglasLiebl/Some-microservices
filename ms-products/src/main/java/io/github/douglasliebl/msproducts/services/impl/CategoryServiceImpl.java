package io.github.douglasliebl.msproducts.services.impl;

import io.github.douglasliebl.msproducts.dto.CategoryDTO;
import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.exception.ResourceNotFoundException;
import io.github.douglasliebl.msproducts.model.entity.Category;
import io.github.douglasliebl.msproducts.model.repositories.CategoryRepository;
import io.github.douglasliebl.msproducts.services.CategoryService;
import io.github.douglasliebl.msproducts.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final ProductService service;

    @Override
    public CategoryDTO registerCategory(CategoryDTO request) {
        if (repository.existsByName(request.getName())) throw new DataIntegrityViolationException("Category name already registered.");

        return CategoryDTO.of(repository
                .save(Category.of(request)));
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO request) {
        if (request == null)
            throw new IllegalArgumentException("Update data cannot be null.");

        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setName(request.getName());

        return CategoryDTO.of(repository.save(category));
    }

    @Override
    public String delete(Long id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id)));

        return "Category successfully deleted.";
    }

    @Override
    public PageImpl<ProductDTO> getProductsByCategory(Long id, Pageable pageRequest) {
        List<ProductDTO> response = service.findByCategory(getById(id), pageRequest).stream()
                .map(ProductDTO::of)
                .toList();

        return new PageImpl<>(response, pageRequest, response.size());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return repository.findAll().stream()
                .map(CategoryDTO::of).toList();
    }

    private Category getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
