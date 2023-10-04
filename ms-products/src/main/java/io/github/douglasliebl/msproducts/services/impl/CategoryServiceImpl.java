package io.github.douglasliebl.msproducts.services.impl;

import io.github.douglasliebl.msproducts.dto.CategoryDTO;
import io.github.douglasliebl.msproducts.exceptions.ResourceNotFoundException;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public Category registerCategory(Category request) {
        if (repository.existsByName(request.getName())) throw new DataIntegrityViolationException("Category name already registered.");
        return repository.save(request);
    }

    @Override
    public Optional<Category> getById(Long id) {
        return Optional.of(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id)));
    }

    @Override
    public Category update(Category actualCategory, CategoryDTO request) {
        if (repository.existsByName(request.getName()) || actualCategory.getId() == null || request.getName() == null)
            throw new IllegalArgumentException("Update data or actual category cannot be null or equal to an already" +
                    " registered category.");

        actualCategory.setName(request.getName());
        return repository.save(actualCategory);
    }

    @Override
    public void delete(Category category) {
        if (category == null || category.getId() == null) throw new IllegalArgumentException("Category cannot be null");
        repository.delete(category);
    }

    @Override
    public Page<Category> find(String name, Pageable pageRequest) {
        Example example = Example.of(Category.builder().name(name).build(),
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example, pageRequest);
    }
}
