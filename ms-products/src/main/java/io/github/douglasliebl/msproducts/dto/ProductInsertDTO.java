package io.github.douglasliebl.msproducts.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInsertDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long manufacturerId;
    private Set<Long> categories;
}
