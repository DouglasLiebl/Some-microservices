package io.github.douglasliebl.msproducts.dto;

import io.github.douglasliebl.msproducts.model.entity.Category;
import io.github.douglasliebl.msproducts.model.entity.Product;
import lombok.*;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String manufacturerName;
    private Set<Category> categories;

    public static ProductDTO of(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .manufacturerName(product.getManufacturer().getName())
                .categories(product.getCategories())
                .build();
    }
}
