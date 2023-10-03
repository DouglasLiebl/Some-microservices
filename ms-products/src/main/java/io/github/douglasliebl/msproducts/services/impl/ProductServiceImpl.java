package io.github.douglasliebl.msproducts.services.impl;

import ch.qos.logback.core.sift.AppenderFactoryUsingSiftModel;
import io.github.douglasliebl.msproducts.dto.ProductInsertDTO;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import io.github.douglasliebl.msproducts.exceptions.ResourceNotFoundException;
import io.github.douglasliebl.msproducts.model.entity.Category;
import io.github.douglasliebl.msproducts.model.entity.Product;
import io.github.douglasliebl.msproducts.model.repositories.CategoryRepository;
import io.github.douglasliebl.msproducts.model.repositories.ManufacturerRepository;
import io.github.douglasliebl.msproducts.model.repositories.ProductRepository;
import io.github.douglasliebl.msproducts.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Override
    public Product createProduct(ProductInsertDTO request) {
        manufacturerVerify(request.getManufacturerId());
        categoryVerify(request.getCategories());

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setManufacturer(manufacturerRepository.findById(request.getManufacturerId())
                .orElseThrow());

        List<Category> categories = categoryRepository.findAllById(request.getCategories());
        product.setCategories(new HashSet<>(categories));

        return productRepository.save(product);
    }

    @Override
    public Product update(Product actualProduct, ProductUpdateDTO updateData) {
        if (actualProduct.getId() == null || updateData ==  null)
            throw new IllegalArgumentException("Update data or actual book cannot be null.");

        actualProduct.setName(updateData.getName());
        actualProduct.setDescription(updateData.getDescription());
        actualProduct.setPrice(updateData.getPrice());

        return productRepository.save(actualProduct);
    }

    @Override
    public Optional<Product> getById(Long id) {
        return Optional.of(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id)));
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
