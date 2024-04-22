package io.github.douglasliebl.msorders.model.entity;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private Long quantity;
}
