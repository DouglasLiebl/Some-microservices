package io.github.douglasliebl.msproducts.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.douglasliebl.msproducts.dto.ProductUpdateDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    @JoinColumn(name = "manufacturer_id")
    @ManyToOne
    @JsonManagedReference
    private Manufacturer manufacturer;

    @ManyToMany
    @JoinTable(name = "tb_products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();


    public static Product of(Product actual, ProductUpdateDTO productUpdate) {
        return Product.builder()
                .id(actual.getId())
                .name(productUpdate.getName())
                .description(productUpdate.getDescription())
                .price(productUpdate.getPrice())
                .manufacturer(actual.getManufacturer())
                .categories(actual.getCategories())
                .build();
    }
}
