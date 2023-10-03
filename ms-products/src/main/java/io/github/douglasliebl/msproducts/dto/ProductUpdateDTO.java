package io.github.douglasliebl.msproducts.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDTO {

    private String name;
    private String description;
    private BigDecimal price;

}
