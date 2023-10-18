package io.github.douglasliebl.msproducts.dto;

import io.github.douglasliebl.msproducts.model.entity.Category;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;

    public static CategoryDTO of(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
