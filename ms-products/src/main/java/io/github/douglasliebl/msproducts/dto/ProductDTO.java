package io.github.douglasliebl.msproducts.dto;

import io.github.douglasliebl.msproducts.model.entity.Category;
import lombok.*;

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

}
