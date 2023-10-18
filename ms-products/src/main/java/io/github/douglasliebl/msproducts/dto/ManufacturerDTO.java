package io.github.douglasliebl.msproducts.dto;

import io.github.douglasliebl.msproducts.model.entity.Manufacturer;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerDTO {

    private Long id;
    private String name;
    private String cnpj;
    private String email;
    private String phoneNumber;

    public static ManufacturerDTO of(Manufacturer manufacturer) {
        return ManufacturerDTO.builder()
                .id(manufacturer.getId())
                .name(manufacturer.getName())
                .cnpj(manufacturer.getCnpj())
                .email(manufacturer.getEmail())
                .phoneNumber(manufacturer.getPhoneNumber())
                .build();
    }
}
