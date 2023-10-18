package io.github.douglasliebl.msproducts.services.impl;

import io.github.douglasliebl.msproducts.dto.ProductDTO;
import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import io.github.douglasliebl.msproducts.exceptions.ResourceNotFoundException;
import io.github.douglasliebl.msproducts.model.entity.Manufacturer;
import io.github.douglasliebl.msproducts.model.entity.Product;
import io.github.douglasliebl.msproducts.model.repositories.CategoryRepository;
import io.github.douglasliebl.msproducts.model.repositories.ManufacturerRepository;
import io.github.douglasliebl.msproducts.model.repositories.ProductRepository;
import io.github.douglasliebl.msproducts.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Override
    public ProductDTO createProduct(ProductInsertDTO request) {
        manufacturerVerify(request.getManufacturerId());
        categoryVerify(request.getCategories());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .manufacturer(manufacturerRepository.findById(request.getManufacturerId())
                        .orElseThrow())
                .categories(new HashSet<>(categoryRepository.findAllById(request.getCategories())))
                .build();

        return ProductDTO.of(productRepository.save(product));
    }

    @Override
    public ProductDTO update(Long id, ProductUpdateDTO updateData) {
        if (updateData == null)
            throw new IllegalArgumentException("Update data cannot be null.");

        Product actualProduct = getProduct(id);

        return ProductDTO.of(productRepository
                .save(Product.of(actualProduct, updateData)));
    }

    @Override
    public ProductDTO getById(Long id) {
        return ProductDTO.of(getProduct(id));
    }

    @Override
    public String delete(Long id) {
        var product = getProduct(id);
        productRepository.delete(product);

        return "Product successfully deleted.";
    }

    @Override
    public Page<Product> find(String name, Pageable pageRequest) {
        Example<Product> example = Example.of(Product.builder().name(name).build(),
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return productRepository.findAll(example, pageRequest);
    }

    @Override
    public Page<Product> findByManufacturer(Manufacturer manufacturer, Pageable pageRequest) {
        return productRepository.findByManufacturer(manufacturer, pageRequest);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private void manufacturerVerify(Long id) {
        if (!manufacturerRepository.existsById(id)) throw new ResourceNotFoundException("Manufacturer not registered.");
    }

    private void categoryVerify(Set<Long> ids) {
        List<Long> verify = new ArrayList<>();
        ids.forEach(x -> { if (!categoryRepository.existsById(x)) verify.add(x); });
        if (new HashSet<>(verify).containsAll(ids)) {
            throw new ResourceNotFoundException("Categories not found: " + verify);
        }
    }
}
