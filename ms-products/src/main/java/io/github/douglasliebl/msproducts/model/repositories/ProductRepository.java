package io.github.douglasliebl.msproducts.model.repositories;

import io.github.douglasliebl.msproducts.model.entity.Category;
import io.github.douglasliebl.msproducts.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByManufacturer_Id(Long id, Pageable pageRequest);
    Page<Product> findAllByCategories(Category category, Pageable pageable);

    void deleteAllByManufacturer_Id(Long id);
}
