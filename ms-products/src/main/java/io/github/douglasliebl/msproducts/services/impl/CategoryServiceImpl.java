package io.github.douglasliebl.msproducts.services.impl;

import io.github.douglasliebl.msproducts.dto.CategoryDTO;
import io.github.douglasliebl.msproducts.exception.ResourceNotFoundException;
import io.github.douglasliebl.msproducts.model.entity.Category;
import io.github.douglasliebl.msproducts.model.repositories.CategoryRepository;
import io.github.douglasliebl.msproducts.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public CategoryDTO registerCategory(CategoryDTO request) {
        if (repository.existsByName(request.getName())) throw new DataIntegrityViolationException("Category name already registered.");

        return CategoryDTO.of(repository
                .save(Category.of(request)));
    }

    @Override
    public CategoryDTO getById(Long id) {
        return CategoryDTO.of(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id)));
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
    public Page<Category> find(String name, Pageable pageRequest) {
        Example<Category> example = Example.of(Category.builder().name(name).build(),
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example, pageRequest);
    }
}
